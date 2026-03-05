package com.expedienteclinico.expedienteclinico.seeders;

import com.expedienteclinico.expedienteclinico.models.system.StatusModel;
import com.expedienteclinico.expedienteclinico.models.system.SystemRolesModel;
import com.expedienteclinico.expedienteclinico.models.system.SystemUsersModel;
import com.expedienteclinico.expedienteclinico.repositories.system.IStatusRepository;
import com.expedienteclinico.expedienteclinico.repositories.system.ISystemRolesRepository;
import com.expedienteclinico.expedienteclinico.repositories.system.ISystemUsersRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Order(2)
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
        resolveUser("admin_lex", "Admin$Secure2026", adminRole, activeStatus); // Profe Alexander
        resolveUser("gestor_rpbi", "Darikson$Secure2026", rpbiRole, activeStatus); // Darikson
        resolveUser("gestor_rrhh", "Olan$Secure2026", rrhhRole, activeStatus); // Olan
        resolveUser("gestor_patients", "Natalia$Secure2026", patientsRole, activeStatus); // Natalia
        resolveUser("gestor_emergencias", "Giselle$Secure2026", emergenciasRole, activeStatus); // Giselle
        resolveUser("gestor_morgue", "Eduardo$Secure2026", morgueRole, activeStatus); // Eduardo
        resolveUser("gestor_lyr", "Julio$Secure2026", lyrRole, activeStatus); // Julio
        resolveUser("gestor_appointments", "Jose$Secure2026", appointmentsRole, activeStatus); // Jose
        resolveUser("gestor_almacen", "Jesus$Secure2026", almacenRole, activeStatus); // Jesus
        resolveUser("gestor_katia", "Katia$Secure2026", katiaRole, activeStatus); // Katia


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

    private void resolveUser(String username, String rawPassword, SystemRolesModel role, StatusModel status) {
        usersRepository.findByUserName(username)
                .orElseGet(() -> {
                    SystemUsersModel user = new SystemUsersModel();
                    user.setUserName(username);
                    // SEGURIDAD: Hashing estricto antes de guardar
                    user.setUserPassword(passwordEncoder.encode(rawPassword));
                    user.setRole(role);
                    user.setStatus(status);
                    return usersRepository.save(user);
                });
    }
}