package com.nexusbusiness.config;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Component
public class PosTenantFlywayOrchestrator implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(PosTenantFlywayOrchestrator.class);
    private final DataSource dataSource;

    public PosTenantFlywayOrchestrator(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void run(ApplicationArguments args) {
        log.info("Starting POS Tenant Flyway orchestration...");
        List<String> tenants = getTenantsFromMaster();

        for (String tenant : tenants) {
            log.info("Migrating schema for new POS system: {}", tenant);

            Flyway flywayTenant = Flyway.configure()
                    .dataSource(dataSource)
                    .schemas(tenant)
                    .locations("classpath:migration/tenants")
                    .baselineOnMigrate(true)
                    .load();

            flywayTenant.migrate();
        }
        log.info("POS Multi-Tenant migrations completed successfully.");
    }

    private List<String> getTenantsFromMaster() {
        List<String> tenants = new ArrayList<>();
        // Note: Querying the master schema to find clinical tenants only
        String query = "SELECT DISTINCT tenant_id FROM his_master.system_users " +
                "WHERE tenant_id NOT IN ('his_master', 'system') AND tenant_id IS NOT NULL";

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query)) {

            while (rs.next()) {
                tenants.add(rs.getString("tenant_id"));
            }
        } catch (SQLException e) {
            log.warn("Could not retrieve tenant list. If this is a fresh install, this is expected.");
            return tenants;
        }
        return tenants;
    }
    /**
     * MIGRACIÓN EN CALIENTE (ON-DEMAND)
     * Metodo público invocado por el HisTenantEventListener cuando RabbitMQ recibe un evento.
     */
    public void migrateSingleTenant(String tenantKey) {
        // El log.info asume que tienes un logger instanciado, ya sea por @Slf4j o manual
        log.info(">>> [RABBITMQ TRIGGER] Materializando infraestructura de negocio en caliente para el inquilino: {}", tenantKey);

        try {
            Flyway flywayTenant = Flyway.configure()
                    .dataSource(dataSource) // Usa el DataSource maestro inyectado en la clase
                    .schemas(tenantKey)     // Apunta estrictamente al esquema recién creado por CORE
                    .locations("classpath:migration/tenants") // La ruta de tus scripts clínicos
                    .baselineOnMigrate(true)
                    .outOfOrder(true)
                    .load();

            flywayTenant.migrate();
            log.info(">>> [ÉXITO] Estructura de tablas de negocio generada para: {}", tenantKey);

        } catch (Exception e) {
            log.error(">>> [ERROR CRÍTICO] Fallo al materializar la estructura de negocio para {}. Causa: {}", tenantKey, e.getMessage());
            throw new RuntimeException("Fallo en aprovisionamiento", e);
        }
    }
}