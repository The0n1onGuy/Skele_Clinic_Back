package com.expedienteclinico.expedienteclinico.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TenantContext {

    private static final Logger logger = LoggerFactory.getLogger(TenantContext.class);
    private static final ThreadLocal<String> CURRENT_TENANT = new ThreadLocal<>();
    private static final String DEFAULT_TENANT = "dbo"; // Esquema maestro por defecto

    public static void setCurrentTenant(String tenant) {
        logger.debug("Asignando tenant al hilo actual: {}", tenant);
        CURRENT_TENANT.set(tenant);
    }

    public static String getCurrentTenant() {
        String tenant = CURRENT_TENANT.get();
        return (tenant != null) ? tenant : DEFAULT_TENANT;
    }

    public static void clear() {
        CURRENT_TENANT.remove();
        logger.debug("TenantContext limpiado para el hilo actual");
    }
}