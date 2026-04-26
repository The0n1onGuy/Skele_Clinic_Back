package com.nexushiscore.config; // Ajusta este paquete según tu estructura exacta en HIS

import com.nexussharedcore.security.TenantConnectionProvider;
import com.nexussharedcore.security.TenantIdentifierResolver;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Map;

@Configuration
public class HibernateConfigPostProcessor {

    @Bean
    public TenantIdentifierResolver tenantIdentifierResolver() {
        return new TenantIdentifierResolver();
    }

    @Bean
    public TenantConnectionProvider tenantConnectionProvider(DataSource dataSource) {
        return new TenantConnectionProvider(dataSource);
    }

    /**
     * ESTA ES LA SOLUCIÓN ARQUITECTÓNICA:
     * Intercepta la configuración de Hibernate en el momento de arranque y le
     * inyecta los objetos vivos manejados por Spring (con el DataSource ya resuelto)
     * en lugar de dejar que Hibernate intente crearlos usando constructores vacíos.
     */
    @Bean
    public HibernatePropertiesCustomizer hibernateCustomizer(
            TenantConnectionProvider connectionProvider,
            TenantIdentifierResolver identifierResolver) {

        return (Map<String, Object> hibernateProperties) -> {
            hibernateProperties.put("hibernate.multi_tenant_connection_provider", connectionProvider);
            hibernateProperties.put("hibernate.tenant_identifier_resolver", identifierResolver);
        };
    }
}