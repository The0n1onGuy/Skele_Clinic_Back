package com.nexusbusiness.seeders;

import com.nexuscore.security.TenantContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

// @Component
@Profile("dev")
@Order(5)
public class PosDataLoader implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(PosDataLoader.class);

    private final DataSource dataSource;


    public PosDataLoader(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void run(ApplicationArguments args) {
        List<String> tenants = getTenants();

        for (String tenant : tenants) {
            try {
                // 1. Cambiamos contexto
                TenantContext.setCurrentTenant(tenant);
                log.info(">>>> INICIANDO INYECCIÓN EN: {}", tenant);


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