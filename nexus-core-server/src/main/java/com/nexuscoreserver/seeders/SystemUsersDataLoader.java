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
public class SystemUsersDataLoader implements CommandLineRunner {

    private final ISystemRolesRepository rolesRepository;
    private final ISystemUsersRepository usersRepository;
    private final IStatusRepository statusRepository;
    private final PasswordEncoder passwordEncoder;

    // Se recomienda fuertemente inyectar esto desde el application.yml en el futuro
    @Value("${nexus.admin.default-password:Admin123!}")
    private String defaultAdminPassword;

    public SystemUsersDataLoader(ISystemRolesRepository rolesRepository,
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
        log.info("Iniciando aprovisionamiento de identidades maestras en CORE...");

        // 1. Recuperar Estatus Activo (Ya inyectado por Flyway)
        StatusModel activeStatus = statusRepository.findByStatusNameIgnoreCase("Active")
                .orElseThrow(() -> new IllegalStateException("Fallo Crítico: El catálogo de estatus no fue sembrado por Flyway."));

        // 2. Aprovisionar Rol de Administración Global
        SystemRolesModel masterRole = resolveRole("MASTER", activeStatus);
        SystemRolesModel adminRole = resolveRole("ROLE_ADMIN", activeStatus);
        // 3. Aprovisionar Usuario Administrador Global
        // Nota: tenantId se fija en 'his_master' referenciando la tabla system_tenants
        resolveUser("admin_core", defaultAdminPassword, masterRole, activeStatus, "his_master");

        log.info(">>> Módulo Seguridad CORE: Identidades base operativas.");
    }

    private SystemRolesModel resolveRole(String roleName, StatusModel status) {
        return rolesRepository.findByRoleName(roleName)
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