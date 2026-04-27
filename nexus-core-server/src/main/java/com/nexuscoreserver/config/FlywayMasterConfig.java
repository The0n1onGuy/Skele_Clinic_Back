package com.nexuscoreserver.config;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class FlywayMasterConfig {

    private static final Logger log = LoggerFactory.getLogger(FlywayMasterConfig.class);

    /**
     * El parámetro (initMethod = "migrate") obliga al ciclo de vida de Spring
     * a ejecutar la migración inmediatamente después de construir el objeto,
     * bloqueando el arranque del servidor hasta que la base maestra esté lista.
     */
    @Bean(initMethod = "migrate")
    public Flyway flywayMaster(DataSource dataSource) {
        log.info(">>> FORZANDO INICIALIZACIÓN EXPLÍCITA DE FLYWAY (MASTER SCHEMA)...");

        return Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/migration/master")
                .baselineOnMigrate(true)
                .outOfOrder(true) // Permite tolerancia a fallos de historial previo
                .load();
    }
}