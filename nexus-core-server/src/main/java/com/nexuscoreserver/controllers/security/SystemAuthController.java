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
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/core/v1/auth") // Ajustado al estándar de versiones que venías manejando
@RequiredArgsConstructor
public class SystemAuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final ISystemUsersRepository usersRepository;
    private final dev.samstevens.totp.code.CodeVerifier totpVerifier;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginRequestObject request) {

        try {
            // 1. Aislamiento de Tenant (Contexto Maestro)
            TenantContext.setCurrentTenant("his_master");

            // 2. FACTOR 1: Autenticación base vía Spring Security
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword())
            );

            // 3. Extracción de Topología de Usuario
            SystemUsersModel user = usersRepository.findByUserName(request.getUserName())
                    .orElseThrow(() -> new IllegalStateException("Inconsistencia: Usuario autenticado pero no encontrado."));

            if (!user.getStatus().getStatusName().equalsIgnoreCase("Active")) {
                return ResponseFactory.forbidden("El usuario se encuentra inactivo o suspendido.");
            }

             // ==========================================
            // EVALUACIÓN DE INFRAESTRUCTURA CRÍTICA (BYPASS CLÍNICO)
            // ==========================================
            boolean isSystemCoreUser = user.getTenantId().equalsIgnoreCase("his_master");

            if (isSystemCoreUser) {

                // 4. BIFURCACIÓN DE FLUJO 2FA (TOTP STATE MACHINE) SOLO PARA HIS_MASTER

                // ESCENARIO A: Primera vez (Requiere Enrolamiento)
                if (!user.getIs2faEnabled()) {
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

                // ESCENARIO B: Enrolado, pero falta el código
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

                // ESCENARIO C: Código provisto (Validación Fast-Track)
                if (!totpVerifier.isValidCode(user.getTotpSecret(), request.getTotpCode())) {
                    return ResponseFactory.unauthorized("El código 2FA ingresado es incorrecto o ha expirado.");
                }
            }
            // Si el usuario NO es "his_master", el código ignora todoa el bloque IF anterior
            // y cae directamente aquí (Éxito Absoluto).

            // ==========================================
            // 5. ÉXITO ABSOLUTO: Emisión de Token Maestro
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
                    "tenantDestination", user.getTenantId() // Conserva tu requerimiento original
            ));

        } catch (BadCredentialsException e) {
            System.err.println("[AUTH FAILURE] Intento denegado para usuario: " + request.getUserName() + " | Causa: Credenciales inválidas");
            return ResponseFactory.unauthorized("Credenciales inválidas.");
        } catch (Exception e) {
            System.err.println("[AUTH ERROR CRÍTICO] Causa: " + e.getMessage());
            return ResponseFactory.unauthorized("Fallo en el proceso de autenticación.");
        } finally {
            // Se ejecutará obligatoriamente limpiando el hilo de ejecución actual
            TenantContext.clear();
        }
    }
}