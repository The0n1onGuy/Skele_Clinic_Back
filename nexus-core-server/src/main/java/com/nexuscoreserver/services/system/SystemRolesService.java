package com.nexuscoreserver.services.system;

import com.nexuscoreserver.models.system.StatusModel;
import com.nexuscoreserver.models.system.SystemRolesModel;
import com.nexuscoreserver.repositories.system.ISystemRolesRepository;
import com.nexuscoreserver.repositories.system.IStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SystemRolesService {

    private final ISystemRolesRepository rolesRepository;
    private final IStatusRepository statusRepository;

    @Transactional
    public SystemRolesModel createRole(String roleName) {
        // Normalización del nombre (Seguridad de grado industrial)
        String normalizedName = roleName.trim().toUpperCase().replace(" ", "_");

        if (rolesRepository.findByRoleNameIgnoreCase(normalizedName).isPresent()) {
            throw new IllegalArgumentException("El rol '" + normalizedName + "' ya existe.");
        }

        SystemRolesModel newRole = new SystemRolesModel();
        newRole.setRoleName(normalizedName);

        // Generación de identificador único universal
        newRole.setUuid(UUID.randomUUID().toString());

        // Asignación de estatus activo por defecto (Homologado al Seeder)
        StatusModel activeStatus = statusRepository.findByStatusNameIgnoreCase("Active")
                .orElseThrow(() -> new IllegalStateException("Estado base 'Active' no configurado en el sistema."));
        newRole.setStatus(activeStatus);

        return rolesRepository.save(newRole);
    }
}