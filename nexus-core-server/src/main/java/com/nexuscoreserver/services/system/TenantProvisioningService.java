package com.nexuscoreserver.services.system;

import com.nexuscoreserver.beans.system.TenantProvisioningRequestObject;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.UUID;


@Service
public class TenantProvisioningService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DataSource masterDataSource; // El pool principal que apunta a his_master

    // Credenciales extraídas de tu entorno para conexiones dinámicas
    @Value("${spring.datasource.username:his_admin}")
    private String dbUser;

    @Value("${spring.datasource.password:Darick471}")
    private String dbPassword;

    @Value("${spring.datasource.url}")
    private String masterUrl;

    /**
     * Orquestador principal. Debe ejecutarse SIN transacciones globales previas
     * para permitir la creación de bases de datos físicas.
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void provisionNewTenant(TenantProvisioningRequestObject request) {
        String tenantKey = request.getTenantKey();

        System.out.println("=== INICIANDO APROVISIONAMIENTO: " + tenantKey + " ===");

        // FASE 1: Base de Datos Física
        createPhysicalDatabase(tenantKey);

        // FASE 2: Flyway
        executeFlywayMigration(tenantKey);

        // FASE 3: Registro en el Directorio Maestro (con Auditoría)
        registerTenantInMaster(request);

        // FASE 4: Inyección del Usuario Administrador del Cliente
        injectTenantAdmin(request);

        System.out.println("=== HOSPITAL " + tenantKey + " APROVISIONADO Y REGISTRADO ===");
    }

    private void injectTenantAdmin(TenantProvisioningRequestObject request) {
        System.out.println("-> Fase 4: Creando credenciales del Administrador para " + request.getTenantKey());
        try {
            JdbcTemplate masterJdbcTemplate = new JdbcTemplate(masterDataSource);

            // Encriptamos la contraseña antes de tocar la base de datos
            String hashedPassword = passwordEncoder.encode(request.getAdminPassword());
            String newUuid = UUID.randomUUID().toString();

            // Usamos subconsultas blindadas para evitar quemar IDs fijos.
            // Buscamos dinámicamente el id_role de 'ROLE_ADMIN' y el id_status de 'Active'
            String sqlUser = "INSERT INTO system_users (role_id, status_id, uuid, tenant_id, user_name, password) " +
                    "VALUES (" +
                    "(SELECT id_role FROM system_roles WHERE role_name = 'ROLE_ADMIN' LIMIT 1), " +
                    "(SELECT id_status FROM status WHERE name = 'Active' LIMIT 1), " +
                    "?, ?, ?, ?)";

            masterJdbcTemplate.update(sqlUser,
                    newUuid,
                    request.getTenantKey(),
                    request.getAdminUsername(),
                    hashedPassword
            );

        } catch (Exception e) {
            throw new RuntimeException("Fallo al crear las credenciales de acceso para el hospital.", e);
        }
    }

    private void registerTenantInMaster(TenantProvisioningRequestObject request) {
        System.out.println("-> Fase 4: Registrando en el Directorio system_tenants...");
        try {
            JdbcTemplate masterJdbcTemplate = new JdbcTemplate(masterDataSource);
            String sql = "INSERT INTO system_tenants (tenant_key, display_name, contact_email) VALUES (?, ?, ?)";
            masterJdbcTemplate.update(sql,
                    request.getTenantKey(),
                    request.getDisplayName(),
                    request.getContactEmail()
            );
        } catch (Exception e) {
            throw new RuntimeException("El hospital se creó, pero falló su registro en el directorio maestro.", e);
        }
    }
    private void createPhysicalDatabase(String tenantName) {
        System.out.println("-> Fase 1: Ejecutando DDL CREATE DATABASE...");
        try (Connection conn = masterDataSource.getConnection();
             Statement stmt = conn.createStatement()) {

            // Crea la base de datos con codificación universal de grado médico
            String sql = "CREATE DATABASE IF NOT EXISTS " + tenantName + " CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";
            stmt.execute(sql);

        } catch (Exception e) {
            throw new RuntimeException("Fallo crítico al crear la base de datos física para " + tenantName, e);
        }
    }

    private void executeFlywayMigration(String tenantName) {
        System.out.println("-> Fase 2: Ejecutando migraciones Flyway...");
        try {
            // Extraemos la URL base (ej. jdbc:mysql://localhost:3306/) y le adjuntamos el nuevo esquema
            String baseUrl = masterUrl.substring(0, masterUrl.lastIndexOf("/") + 1);
            String tenantUrl = baseUrl + tenantName + "?useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC";

            // Instanciamos un Flyway independiente y efímero solo para este inquilino
            Flyway flywayTenant = Flyway.configure()
                    .dataSource(tenantUrl, dbUser, dbPassword)
                    // ASUNCIÓN: Aquí debes poner la ruta de tu script SQL que contiene las tablas para los clientes (pacientes, citas, etc.)
                    // NO uses el V1__Master_Schema.sql, debe ser el esquema de hospitales.
                    .locations("classpath:db/migration/tenants")
                    .baselineOnMigrate(true)
                    .load();

            flywayTenant.migrate();

        } catch (Exception e) {
            throw new RuntimeException("Fallo crítico al migrar tablas para " + tenantName, e);
        }
    }
}