package com.expedienteclinico.expedienteclinico.security;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Component
@Order(1) // Garantiza prioridad máxima de ejecución
public class FlywayMultiTenantOrchestrator implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(FlywayMultiTenantOrchestrator.class);
    private final DataSource dataSource;

    public FlywayMultiTenantOrchestrator(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void run(ApplicationArguments args) {
        log.info("Iniciando orquestación de migraciones Flyway...");

// 1. MIGRACIÓN EXPLÍCITA Y OBLIGATORIA DE LA BASE DE DATOS MAESTRA
        log.info("Aprovisionando infraestructura MAESTRA (his_rpbi_master)...");
        Flyway flywayMaster = Flyway.configure()
                .dataSource(dataSource) // El pool principal ya apunta a su destino
                .locations("classpath:db/migration/master") // Apunta a la nueva carpeta
                .baselineOnMigrate(true)
                .load();
        flywayMaster.migrate();

        // 2. MIGRACIÓN FÍSICA DE BASES DE DATOS DE INQUILINOS (DATABASE-PER-TENANT)
        com.zaxxer.hikari.HikariDataSource hikariDs = (com.zaxxer.hikari.HikariDataSource) dataSource;
        String masterUrl = hikariDs.getJdbcUrl();
        log.info("Migración del esquema maestro (his_rpbi_master) completada.");

        // 2. OBTENER DIRECTORIO DE INQUILINOS
        List<String> tenants = getTenantsFromMaster();

        // 3. MIGRACIÓN DE ESQUEMAS CLIENTES
        for (String tenant : tenants) {
            log.info("Migrando esquema de inquilino: {}", tenant);

            // INYECCIÓN EXPLÍCITA DEL IDENTIFICADOR (Placeholders)
            // Esto cubre tanto si usaste guion bajo como si usaste dos puntos en tu SQL
            Map<String, String> tenantPlaceholders = new HashMap<>();
            tenantPlaceholders.put("tenant_Schema", tenant);
            tenantPlaceholders.put("tenant_Schema", tenant);

            Flyway flywayTenant = Flyway.configure()
                    .dataSource(dataSource)
                    .schemas(tenant)
                    .placeholders(tenantPlaceholders) // <-- INYECCIÓN DE LA VARIABLE AL MOTOR
                    .locations("classpath:db/migration/tenants")
                    .baselineOnMigrate(true)
                    .load();
            flywayTenant.migrate();
        }
        log.info("Todas las migraciones Multi-Tenant finalizaron correctamente.");
    }

    private List<String> getTenantsFromMaster() {
        List<String> tenants = new ArrayList<>();
        String query = "SELECT DISTINCT tenant_id FROM his_rpbi_master.system_users WHERE tenant_id != 'his_rpbi_master' AND tenant_id IS NOT NULL";

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query)) {

            while (rs.next()) {
                tenants.add(rs.getString("tenant_id"));
            }
        } catch (SQLException e) {
            // Si la tabla no existe (ej. base vacía), no abortar, retornar lista vacía.
            if(e.getMessage().contains("Invalid object name 'his_rpbi_master.system_users'")) {
                return tenants;
            }
            log.error("CRÍTICO: Fallo al leer el directorio de inquilinos del esquema maestro.", e);
            throw new RuntimeException("Fallo en inicialización de Flyway", e);
        }
        return tenants;
    }
}