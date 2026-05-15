package com.nexuscoreserver.controllers.system;

import com.nexuscoreserver.beans.system.SystemUsersRequestObject;
import com.nexuscoreserver.models.system.SystemRolesModel;
import com.nexuscoreserver.models.system.SystemUsersModel;
import com.nexuscoreserver.repositories.system.ISystemRolesRepository;
import com.nexuscoreserver.services.system.SystemUsersService;
import com.nexussharedcore.payload.response.ResponseFactory;
import com.nexussharedcore.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/core/v1/system-users")
@RequiredArgsConstructor
public class SystemUsersController {

    private final SystemUsersService systemUsersService;
    private final ISystemRolesRepository systemRolesRepository;
    private final JwtService jwtService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createSystemUser(
            @Valid @RequestBody SystemUsersRequestObject request,
            HttpServletRequest httpRequest) {

        // 1. Auditoría y Extracción desde el Contexto
        org.springframework.security.core.Authentication authentication =
                org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();

        // Limpiamos y comparamos de forma robusta
        boolean isMaster = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().trim().equalsIgnoreCase("ROLE_MASTER"));

        boolean isAdminClinica = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().trim().equalsIgnoreCase("ROLE_ADMIN"));

        System.out.println("[DEBUG AUTH] isMaster: " + isMaster + " | isAdminClinica: " + isAdminClinica);

        String token = httpRequest.getHeader("Authorization").substring(7);
        String userTenant = jwtService.extractTenant(token);
        String targetTenant = request.getTenantId() != null ? request.getTenantId() : "his_master";

        // 2. LÓGICA DE FILTRO CON LOGS DE SALIDA
        if (!isMaster) {
            if (isAdminClinica) {
                // Validación de Jurisdicción
                if (!userTenant.equalsIgnoreCase(targetTenant)) {
                    System.out.println("[AUTH DENIED] Intento de cruce de tenants: " + userTenant + " -> " + targetTenant);
                    return ResponseFactory.forbidden("No puede crear usuarios para otra clínica.");
                }
                // Validación de Escalada de Privilegios
                if (request.getRoleName().equalsIgnoreCase("MASTER") ||
                        request.getRoleName().equalsIgnoreCase("DEVELOPER") ||
                        request.getRoleName().equalsIgnoreCase("ADMIN")) {
                    return ResponseFactory.forbidden("No tiene permisos para asignar roles de infraestructura crítica.");
                }
            } else {
                // ESTE ES EL BLOQUE DONDE ESTÁS CAYENDO
                System.out.println("[AUTH DENIED] El usuario no es MASTER ni ADMIN.");
                return ResponseFactory.forbidden("Acceso denegado: Privilegios insuficientes.");
            }
        }

        // 3. Ejecución
        SystemRolesModel assignedRole = systemRolesRepository.findByRoleNameIgnoreCase(request.getRoleName())
                .orElseThrow(() -> new RuntimeException("Error de Integridad: El rol '" + request.getRoleName() + "' no existe en la base maestra de CORE."));

        SystemUsersModel createdUser = systemUsersService.createSystemUsers(request, assignedRole, targetTenant);

        return ResponseFactory.created("Usuario creado exitosamente.", Map.of(
                "userName", createdUser.getUserName(),
                "assignedTenant", createdUser.getTenantId()
        ));
    }
}