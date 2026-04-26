package com.nexussharedcore.security;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;


public class TenantIdentifierResolver implements CurrentTenantIdentifierResolver<String> {

    @Override
    public String resolveCurrentTenantIdentifier() {
        String current = TenantContext.getCurrentTenant();
        System.out.println(">>> [PASO 2 - HIBERNATE] Hibernate pregunta el esquema. Respondiendo: " + current);
        return current;
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}