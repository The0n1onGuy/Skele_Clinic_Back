package com.nexuscore.seeders;

import com.nexuscore.models.system.StatusModel;
import com.nexuscore.models.system.SystemRolesModel;
import com.nexuscore.models.system.SystemUsersModel;
import com.nexuscore.repositories.system.IStatusRepository;
import com.nexuscore.repositories.system.ISystemRolesRepository;
import com.nexuscore.repositories.system.ISystemUsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.nexuscore.security.TenantContext;

@Slf4j
@Component
@Order(4)
public class SystemUsersDataLoader implements CommandLineRunner {

    private final ISystemRolesRepository rolesRepository;
    private final ISystemUsersRepository usersRepository;
    private final IStatusRepository statusRepository;
    private final PasswordEncoder passwordEncoder;

    public SystemUsersDataLoader(ISystemRolesRepository rolesRepository, ISystemUsersRepository usersRepository, IStatusRepository statusRepository, PasswordEncoder passwordEncoder) {
        this.rolesRepository = rolesRepository;
        this.usersRepository = usersRepository;
        this.statusRepository = statusRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        TenantContext.setCurrentTenant("his_master");
        try {
        StatusModel activeStatus = statusRepository.findById(1L)
                .orElseThrow(() -> new IllegalStateException("Error crítico: Estatus Activo no encontrado. Verifica SystemDataLoader."));

        // 1. Sembrar Roles
        SystemRolesModel adminRole = resolveRole("ROLE_ADMIN", activeStatus);
        SystemRolesModel rpbiRole = resolveRole("ROLE_RPBI", activeStatus);
        SystemRolesModel rrhhRole = resolveRole("ROLE_RRHH", activeStatus);
        SystemRolesModel patientsRole = resolveRole("ROLE_PATIENTS", activeStatus);
        SystemRolesModel emergenciasRole = resolveRole("ROLE_EMERGENCIAS", activeStatus);
        SystemRolesModel morgueRole = resolveRole("ROLE_MORGUE", activeStatus);
        SystemRolesModel lyrRole = resolveRole("ROLE_LYR", activeStatus);
        SystemRolesModel appointmentsRole = resolveRole("ROLE_APPOINTMENTS", activeStatus);
        SystemRolesModel almacenRole = resolveRole("ROLE_ALMACEN", activeStatus);
        SystemRolesModel katiaRole = resolveRole("ROLE_KATIA", activeStatus);
        SystemRolesModel devRole = resolveRole("ROLE_DEVELOPMENT", activeStatus);

        // 2. Sembrar Usuarios (Cambiando "password123" por contraseñas seguras según sus políticas)
        resolveUser("admin_lex", "Admin$Secure2026", adminRole, activeStatus, "hospital_prueba"); // Profe Alexander
        resolveUser("gestor_rpbi", "Darikson$Secure2026", rpbiRole, activeStatus, "hospital_prueba"); // Darikson
        resolveUser("gestor_rrhh", "Olan$Secure2026", rrhhRole, activeStatus, "hospital_prueba"); // Olan
        resolveUser("gestor_patients", "Natalia$Secure2026", patientsRole, activeStatus, "hospital_prueba"); // Natalia
        resolveUser("gestor_emergencias", "Giselle$Secure2026", emergenciasRole, activeStatus, "hospital_prueba"); // Giselle
        resolveUser("gestor_morgue", "Eduardo$Secure2026", morgueRole, activeStatus, "hospital_prueba"); // Eduardo
        resolveUser("gestor_lyr", "Julio$Secure2026", lyrRole, activeStatus, "hospital_prueba"); // Julio
        resolveUser("gestor_appointments", "Jose$Secure2026", appointmentsRole, activeStatus, "hospital_prueba"); // Jose
        resolveUser("gestor_almacen", "Jesus$Secure2026", almacenRole, activeStatus, "hospital_prueba"); // Jesus
        resolveUser("gestor_katia", "Katia$Secure2026", katiaRole, activeStatus, "hospital_prueba"); // Katia

        // 2. INYECCIÓN DE CLIENTES (TENANTS DE PRUEBA)
        // Al crear estos usuarios, registramos formalmente la existencia de los hospitales en el Directorio Maestro
        resolveUser("admin_aurora", "Aurora123!", adminRole, activeStatus, "hospital_aurora");
        resolveUser("admin_general", "General123!", adminRole, activeStatus, "hospital_general");

        } catch (Exception e) {
            log.error("Error cargando datos semilla", e);
        } finally {
            // 2. LIMPIEZA OBLIGATORIA
            TenantContext.clear();
        }

        System.out.println(">>> Módulo Seguridad: Roles y Usuarios de Desarrolladores base sembrados con éxito.");


        }

    private SystemRolesModel resolveRole(String roleName, StatusModel status) {
        return rolesRepository.findByRoleName(roleName)
                .orElseGet(() -> {
                    SystemRolesModel role = new SystemRolesModel();
                    role.setRoleName(roleName);
                    role.setStatus(status);
                    return rolesRepository.save(role);
                });
    }

    private void resolveUser(String username, String rawPassword, SystemRolesModel role, StatusModel status, String tenantId) {
        usersRepository.findByUserName(username)
                .orElseGet(() -> {
                    SystemUsersModel user = new SystemUsersModel();
                    user.setTenantId(tenantId); // ASIGNACIÓN DINÁMICA DEL CLIENTE
                    user.setUserName(username);
                    // SEGURIDAD: Asegurar que se use passwordEncoder (vi que en tu captura le faltaba el .encode)
                    user.setUserPassword(passwordEncoder.encode(rawPassword));
                    user.setRole(role);
                    user.setStatus(status);

                    return usersRepository.save(user);
                });
    }
}