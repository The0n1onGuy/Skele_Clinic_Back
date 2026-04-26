package com.nexuscoreserver.controllers.system;

import com.nexuscoreserver.beans.system.SystemUsersRequestObject;
import com.nexuscoreserver.models.system.SystemUsersModel;
import com.nexuscoreserver.repositories.system.ISystemUsersRepository;
import com.nexussharedcore.security.JwtService; // PAQUETE CORREGIDO
import com.nexussharedcore.security.TenantContext; // PAQUETE CORREGIDO
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
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
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class SystemAuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final ISystemUsersRepository usersRepository;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody SystemUsersRequestObject request) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Se fuerza el contexto a la base maestra para validar identidad
            TenantContext.setCurrentTenant("his_master");

            // 1. Autenticar credenciales contra la base de datos a través del Provider
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUserName(), request.getUserPassword())
            );

            // 2. Extraer el modelo del usuario desde la BD para obtener su Tenant ID
            SystemUsersModel user = usersRepository.findByUserName(request.getUserName())
                    .orElseThrow(() -> new RuntimeException("Inconsistencia de datos: Usuario autenticado pero no encontrado en registros"));

            // 3. EXTRACCIÓN DE ROLES COMO LISTA (Cumplimiento de contrato con JwtService)
            List<String> roles = auth.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            // 4. EMPAQUETADO ESTRICTO DE CLAIMS
            Map<String, Object> extraClaims = new HashMap<>();
            extraClaims.put("roles", roles); // Llave plural obligatoria
            extraClaims.put("tenant", user.getTenantId());

            // 5. Generación de JWT (Firma criptográfica)
            String jwtToken = jwtService.generateToken(extraClaims, request.getUserName());

            response.put("message", "Autenticación exitosa");
            response.put("token", jwtToken);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Traza interna para auditoría del equipo de desarrollo (evita perder el rastro del error)
            System.err.println("[AUTH FAILURE] Intento denegado para usuario: " + request.getUserName() + " | Causa: " + e.getMessage());

            // Respuesta opaca al cliente para evitar enumeración de usuarios
            response.put("message", "Credenciales inválidas");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        } finally {
            TenantContext.clear();
        }
    }
}