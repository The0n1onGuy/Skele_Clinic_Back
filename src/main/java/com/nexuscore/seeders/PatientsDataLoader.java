package com.nexuscore.seeders;

import com.nexuscore.security.TenantContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

// @Component
@Profile("dev")
@Order(5)
public class PatientsDataLoader implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(PatientsDataLoader.class);

    private final DataSource dataSource;
    private final TenantSeederService seederService; // Inyección del nuevo servicio

    public PatientsDataLoader(DataSource dataSource, TenantSeederService seederService) {
        this.dataSource = dataSource;
        this.seederService = seederService;
    }

    @Override
    public void run(ApplicationArguments args) {
        List<String> tenants = getTenants();

        for (String tenant : tenants) {
            try {
                // 1. Cambiamos contexto
                TenantContext.setCurrentTenant(tenant);
                log.info(">>>> INICIANDO INYECCIÓN EN: {}", tenant);

                // 2. Ejecutamos la siembra a través del servicio transaccional
                seederService.executeSeed(tenant);

                log.info(">>>> FINALIZADO CON ÉXITO EN: {}", tenant);
            } catch (Exception e) {
                log.error("XXXX FALLO en esquema {}: {}", tenant, e.getMessage());
            } finally {
                // 3. Limpieza vital
                TenantContext.clear();
            }
        }
    }

    private List<String> getTenants() {
        List<String> tenants = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT DISTINCT tenant_id FROM his_master.system_users WHERE tenant_id != 'his_master' AND tenant_id IS NOT NULL")) {
            while (rs.next()) {
                tenants.add(rs.getString(1));
            }
        } catch (Exception e) {
            log.error("Fallo al obtener directorio de inquilinos", e);
        }
        return tenants;
    }
}