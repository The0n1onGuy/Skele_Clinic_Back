package com.nexuscoreserver.seeders;

import com.nexuscoreserver.models.system.StatusModel;
import com.nexuscoreserver.models.system.SystemRolesModel;
import com.nexuscoreserver.models.system.SystemUsersModel;
import com.nexuscoreserver.repositories.system.IStatusRepository;
import com.nexuscoreserver.repositories.system.ISystemRolesRepository;
import com.nexuscoreserver.repositories.system.ISystemUsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@Order(4) // Se ejecuta después de Flyway
@Profile("!test")
public class PosUsersDataLoader implements CommandLineRunner {

    private final ISystemRolesRepository rolesRepository;
    private final ISystemUsersRepository usersRepository;
    private final IStatusRepository statusRepository;
    private final PasswordEncoder passwordEncoder;

    // Se recomienda fuertemente inyectar esto desde el application.yml en el futuro
    @Value("${nexus.admin.default-password:Admin123!}")
    private String defaultAdminPassword;

    //Declaring my POS tenant
    @Value("${NEXUS_SEED_TARGET_TENANT:pos_prueba_2026}")
    private String targetTenant;

    public PosUsersDataLoader(ISystemRolesRepository rolesRepository,
                              ISystemUsersRepository usersRepository,
                              IStatusRepository statusRepository,
                              PasswordEncoder passwordEncoder) {
        this.rolesRepository = rolesRepository;
        this.usersRepository = usersRepository;
        this.statusRepository = statusRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (targetTenant == null || targetTenant.trim().isEmpty()) {
            return; // Skip if no tenant is targeted
        }
        log.info("Iniciando aprovisionamiento de identidades maestras en CORE...");

        // Recuperar Estatus Activo (Ya inyectado por Flyway)
        StatusModel activeStatus = statusRepository.findByStatusNameIgnoreCase("Active")
                .orElseThrow(() -> new IllegalStateException("Fallo Crítico: El catálogo de estatus no fue sembrado por Flyway."));

        // Aprovisionar Rol de Administración Global
        SystemRolesModel collaboratorRole = resolveRole("ROLE_POSCOLLABORATOR", activeStatus); //Those who work using the POS
        SystemRolesModel managerRole = resolveRole("ROLE_POSMANAGER", activeStatus); //The ones that MANAGE the POS or Store

        // Aprovisionar Usuario Administrador Global
        // Changed to a POS Tenant IN TEST YET (needs proper dynamic target, not fixed)
        resolveUser("ManagerOnion", defaultAdminPassword, managerRole, activeStatus, targetTenant);
        resolveUser("CollaboratorSkeleton", defaultAdminPassword, collaboratorRole, activeStatus, targetTenant);

        log.info(">>> Módulo Seguridad CORE: Usuarios de POS predeterminados añadidos.");
    }

    private SystemRolesModel resolveRole(String roleName, StatusModel status) {
        return rolesRepository.findByRoleNameIgnoreCase(roleName)
                .orElseGet(() -> {
                    SystemRolesModel role = new SystemRolesModel();
                    role.setRoleName(roleName);
                    role.setStatus(status);
                    role.setUuid(UUID.randomUUID().toString());
                    return rolesRepository.save(role);
                });
    }

    private void resolveUser(String username, String rawPassword, SystemRolesModel role, StatusModel status, String tenantKey) {
        usersRepository.findByUserName(username)
                .orElseGet(() -> {
                    SystemUsersModel user = new SystemUsersModel();
                    user.setTenantId(tenantKey);
                    user.setUserName(username);
                    user.setPassword(passwordEncoder.encode(rawPassword));
                    user.setRole(role);
                    user.setStatus(status);
                    user.setUuid(UUID.randomUUID().toString());

                    // Inicialización de campos TOTP (Fase 2)
                    user.setIs2faEnabled(false);
                    user.setTotpSecret(null);

                    return usersRepository.save(user);
                });
    }
}