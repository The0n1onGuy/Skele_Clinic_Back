package com.nexuscoreserver.controllers.security;

import com.nexuscoreserver.beans.system.LoginRequestObject;
import com.nexuscoreserver.models.system.SystemUsersModel;
import com.nexuscoreserver.repositories.system.ISystemUsersRepository;
import com.nexussharedcore.payload.response.ResponseFactory;
import com.nexussharedcore.security.JwtService;
import com.nexussharedcore.security.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controlador REST para el manejo de sesiones y autenticación en la plataforma Core.
 * Modificado para soportar una política de MFA Dinámica y parametrizable por Tenant,
 * mitigando la fricción del usuario mediante una ventana temporal deslizable (ej: 24h o 8h según el cliente).
 */
@RestController
@RequestMapping("/api/core/v1/auth")
@RequiredArgsConstructor
public class SystemAuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final ISystemUsersRepository usersRepository;
    private final dev.samstevens.totp.code.CodeVerifier totpVerifier;
    private final JdbcTemplate jdbcTemplate; // Inyección de plantilla JDBC para consultas dinámicas directas del Tenant Directory

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginRequestObject request) {

        try {
            // 1. Aislamiento de Tenant (Contexto Maestro)
            TenantContext.setCurrentTenant("his_master");

            // 2. FACTOR 1: Autenticación de credenciales base (Usuario y Contraseña) vía Spring Security
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword())
            );

            // 3. Extracción de la topología y datos del usuario
            SystemUsersModel user = usersRepository.findByUserName(request.getUserName())
                    .orElseThrow(() -> new IllegalStateException("Inconsistencia: Usuario autenticado pero no encontrado."));

            if (!user.getStatus().getStatusName().equalsIgnoreCase("Active")) {
                return ResponseFactory.forbidden("El usuario se encuentra inactivo o suspendido.");
            }

            // ==========================================
            // EVALUACIÓN DE INFRAESTRUCTURA CRÍTICA (POLÍTICA DINÁMICA DE 2FA POR USUARIO Y TENANT)
            // ==========================================
            // Solo los usuarios del tenant 'his_master' (CORE) están obligados a usar 2FA por defecto.
            // Para el resto de inquilinos, se evalúa dinámicamente según si el usuario ya tiene habilitado 2FA en su perfil.
            boolean isSystemCoreUser = user.getTenantId().equalsIgnoreCase("his_master");
            boolean is2faRequired = isSystemCoreUser || (user.getIs2faEnabled() != null && user.getIs2faEnabled());

            if (is2faRequired) {

                // A. Recuperamos de forma dinámica el límite de validez (horas) configurado para este Tenant específico
                int validityHours = 24; // Margen por defecto
                try {
                    String sql = "SELECT totp_validity_hours FROM system_tenants WHERE tenant_key = ?";
                    Integer hours = jdbcTemplate.queryForObject(sql, Integer.class, user.getTenantId());
                    if (hours != null) {
                        validityHours = hours;
                    }
                } catch (Exception e) {
                    System.err.println("[WARN 2FA POLICY] No se pudo resolver totp_validity_hours para el tenant " + user.getTenantId() + ". Fallback: 24h");
                }

                // B. Comprobamos si la ventana temporal deslizable del 2FA está vigente para el usuario actual
                boolean isVerificationValid = false;
                if (user.getLast2faVerifiedAt() != null) {
                    LocalDateTime expiryTime = user.getLast2faVerifiedAt().plusHours(validityHours);
                    isVerificationValid = LocalDateTime.now().isBefore(expiryTime);
                }

                // C. Si la ventana expiró o no existe registro previo, obligamos a pasar por el flujo de 2FA
                if (!isVerificationValid) {

                    // ESCENARIO A: Primera vez o enrolamiento incompleto (El usuario no ha habilitado 2FA definitivamente)
                    if (!user.getIs2faEnabled() || user.getTotpSecret() == null || user.getTotpSecret().trim().isEmpty()) {
                        String preAuthToken = jwtService.generateToken(
                                Map.of("roles", List.of("PRE_AUTH_SETUP"), "tenant", user.getTenantId()),
                                user.getUserName()
                        );
                        return ResponseFactory.successMessage("Requiere configuración de 2FA", Map.of(
                                "authStatus", "REQUIRE_SETUP",
                                "temporaryToken", preAuthToken,
                                "tenantDestination", user.getTenantId()
                        ));
                    }

                    // ESCENARIO B: Enrolamiento activo pero falta proveer el código de 6 dígitos
                    if (request.getTotpCode() == null || request.getTotpCode().trim().isEmpty()) {
                        String preAuthToken = jwtService.generateToken(
                                Map.of("roles", List.of("PRE_AUTH_VERIFY"), "tenant", user.getTenantId()),
                                user.getUserName()
                        );
                        return ResponseFactory.successMessage("Requiere código 2FA", Map.of(
                                "authStatus", "REQUIRE_VERIFICATION",
                                "temporaryToken", preAuthToken,
                                "tenantDestination", user.getTenantId()
                        ));
                    }

                    // ESCENARIO C: Validación en caliente del código provisto
                    if (!totpVerifier.isValidCode(user.getTotpSecret(), request.getTotpCode())) {
                        return ResponseFactory.unauthorized("El código 2FA ingresado es incorrecto o ha expirado.");
                    }

                    // ÉXITO DE VERIFICACIÓN EN CALIENTE: Actualizamos la marca de tiempo local y el flag
                    user.setLast2faVerifiedAt(LocalDateTime.now());
                    if (!user.getIs2faEnabled()) {
                        user.setIs2faEnabled(true);
                    }
                    usersRepository.save(user);
                } else {
                    System.out.println(">>> [2FA BYPASS TEMPORAL ACTIVO] Sesión 2FA de " + user.getUserName() + 
                            " validada exitosamente vía sliding-window (Validez: " + validityHours + " horas).");
                }
            }

            // ==========================================
            // 5. ÉXITO ABSOLUTO: Emisión de Token Maestro final
            // ==========================================
            List<String> roles = auth.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            Map<String, Object> extraClaims = new HashMap<>();
            extraClaims.put("roles", roles);
            extraClaims.put("tenant", user.getTenantId());

            String jwtToken = jwtService.generateToken(extraClaims, request.getUserName());

            return ResponseFactory.successMessage("Autenticación exitosa", Map.of(
                    "authStatus", "SUCCESS",
                    "token", jwtToken,
                    "tenantDestination", user.getTenantId()
            ));

        } catch (BadCredentialsException e) {
            System.err.println("[AUTH FAILURE] Intento denegado para usuario: " + request.getUserName() + " | Causa: Credenciales inválidas");
            return ResponseFactory.unauthorized("Credenciales inválidas.");
        } catch (Exception e) {
            System.err.println("[AUTH ERROR CRÍTICO] Causa: " + e.getMessage());
            return ResponseFactory.unauthorized("Fallo en el proceso de autenticación.");
        } finally {
            // Limpieza del contexto en el hilo actual de ejecución
            TenantContext.clear();
        }
    }
}