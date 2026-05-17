package com.nexuscoreserver.controllers.system;

import com.nexuscoreserver.models.system.SystemRolesModel;
import com.nexuscoreserver.services.system.SystemRolesService;
import com.nexussharedcore.payload.response.ResponseFactory;
import com.nexussharedcore.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/core/v1/system-roles")
@RequiredArgsConstructor
public class SystemRolesController {

    private final SystemRolesService rolesService;
    private final JwtService jwtService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createRole(
            @RequestBody Map<String, String> request,
            HttpServletRequest httpRequest) {

        // 1. Candado de Seguridad Criptográfico (Solo MASTER)
        String authHeader = httpRequest.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseFactory.unauthorized("Token inexistente.");
        }

        String token = authHeader.substring(7);
        if (!jwtService.isTokenSignatureValid(token)) {
            return ResponseFactory.unauthorized("Token inválido.");
        }

        List<GrantedAuthority> authorities = jwtService.extractRoles(token);
        boolean isMaster = authorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_MASTER"));

        if (!isMaster) {
            return ResponseFactory.forbidden("Acceso denegado: Se requiere rol MASTER.");
        }

        // 2. Ejecución de la lógica de negocio
        String roleName = request.get("roleName");
        SystemRolesModel savedRole = rolesService.createRole(roleName);

        return ResponseFactory.created("Rol del sistema generado exitosamente.", Map.of(
                "id", savedRole.getId(),
                "roleName", savedRole.getRoleName(),
                "uuid", savedRole.getUuid()
        ));
    }
}