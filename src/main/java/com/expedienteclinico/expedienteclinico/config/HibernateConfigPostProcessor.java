package com.expedienteclinico.expedienteclinico.config;

import com.expedienteclinico.expedienteclinico.security.TenantConnectionProvider;
import com.expedienteclinico.expedienteclinico.security.TenantIdentifierResolver;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class HibernateConfigPostProcessor implements BeanPostProcessor {

    private final TenantIdentifierResolver tenantResolver;
    private final TenantConnectionProvider tenantConnectionProvider;

    public HibernateConfigPostProcessor(TenantIdentifierResolver tenantResolver, TenantConnectionProvider tenantConnectionProvider) {
        this.tenantResolver = tenantResolver;
        this.tenantConnectionProvider = tenantConnectionProvider;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        // Interceptamos la fábrica de Hibernate justo antes de que arranque
        if (bean instanceof LocalContainerEntityManagerFactoryBean) {
            LocalContainerEntityManagerFactoryBean emfb = (LocalContainerEntityManagerFactoryBean) bean;

            // Extraemos sus propiedades actuales y le inyectamos nuestros enrutadores
            Map<String, Object> properties = new HashMap<>(emfb.getJpaPropertyMap());
            properties.put(AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER, tenantResolver);
            properties.put(AvailableSettings.MULTI_TENANT_CONNECTION_PROVIDER, tenantConnectionProvider);

            emfb.setJpaPropertyMap(properties);
        }
        return bean;
    }
}