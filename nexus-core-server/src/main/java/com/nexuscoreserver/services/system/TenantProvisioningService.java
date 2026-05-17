package com.nexuscoreserver.services.system;

import com.nexuscoreserver.beans.system.TenantProvisioningRequestObject;
import com.nexussharedcore.events.TenantCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.util.UUID;

@Slf4j
@Service
public class TenantProvisioningService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DataSource masterDataSource;

    // >> INYECCIÓN DE LA DEPENDENCIA DEL BROKER
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void provisionNewTenant(TenantProvisioningRequestObject request) {
        String tenantKey = request.getTenantKey();
        log.info("=== STARTING SAAS PROVISIONING FOR TENANT: {} ===", tenantKey);

        // PHASE 1: Physical Schema Creation
        createPhysicalDatabase(tenantKey);

        // PHASE 2: Registration & Subscription (SaaS N:M Model)
        registerTenantAndSubscription(request);

        // PHASE 3: Tenant Admin Injection
        injectTenantAdmin(request);

        // >> FASE 4: NOTIFICACIÓN ASÍNCRONA (COREOGRAFÍA)
        triggerAsynchronousProvisioning(tenantKey, request.getServiceCode());

        log.info("=== TENANT {} PROVISIONED AND SUBSCRIBED TO {} ===", tenantKey, request.getServiceCode());
    }

    private void createPhysicalDatabase(String tenantName) {
        log.info("-> Phase 1: Executing DDL CREATE DATABASE...");
        try (Connection conn = masterDataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            String sql = "CREATE DATABASE IF NOT EXISTS " + tenantName + " CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";
            stmt.execute(sql);
        } catch (Exception e) {
            throw new RuntimeException("Critical failure creating physical database for " + tenantName, e);
        }
    }

    private void registerTenantAndSubscription(TenantProvisioningRequestObject request) {
        log.info("-> Phase 2: Registering Tenant and Service Subscription...");
        JdbcTemplate masterJdbcTemplate = new JdbcTemplate(masterDataSource);

        try {
            // 1. Insert Tenant
            String sqlTenant = "INSERT INTO system_tenants (status_id, tenant_key, display_name, contact_email) " +
                    "VALUES ((SELECT id_status FROM status WHERE name = 'Active' LIMIT 1), ?, ?, ?)";
            masterJdbcTemplate.update(sqlTenant, request.getTenantKey(), request.getDisplayName(), request.getContactEmail());

            // 2. Insert Subscription (N:M)
            String sqlSubscription = "INSERT INTO tenant_subscriptions (id_tenant, id_service, status_id) " +
                    "VALUES (" +
                    "(SELECT id_tenant FROM system_tenants WHERE tenant_key = ? LIMIT 1), " +
                    "(SELECT id_service FROM system_services WHERE service_code = ? LIMIT 1), " +
                    "(SELECT id_status FROM status WHERE name = 'Active' LIMIT 1))";
            masterJdbcTemplate.update(sqlSubscription, request.getTenantKey(), request.getServiceCode());

        } catch (Exception e) {
            throw new RuntimeException("Failed to register tenant or subscription in master directory.", e);
        }
    }

    private void injectTenantAdmin(TenantProvisioningRequestObject request) {
        log.info("-> Phase 3: Creating Administrator credentials for {}", request.getTenantKey());
        try {
            JdbcTemplate masterJdbcTemplate = new JdbcTemplate(masterDataSource);
            String hashedPassword = passwordEncoder.encode(request.getAdminPassword());
            String newUuid = UUID.randomUUID().toString();

            String sqlUser = "INSERT INTO system_users (role_id, status_id, uuid, tenant_id, user_name, password, is_2fa_enabled) " +
                    "VALUES (" +
                    "(SELECT id_role FROM system_roles WHERE role_name = 'ROLE_ADMIN' LIMIT 1), " +
                    "(SELECT id_status FROM status WHERE name = 'Active' LIMIT 1), " +
                    "?, ?, ?, ?, false)";

            masterJdbcTemplate.update(sqlUser, newUuid, request.getTenantKey(), request.getAdminUsername(), hashedPassword);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create access credentials for the tenant.", e);
        }
    }

    // >> IMPLEMENTACIÓN DEL GATILLO ASÍNCRONO
    private void triggerAsynchronousProvisioning(String tenantKey, String serviceCode) {
        log.info("-> Phase 4: Emitiendo evento asíncrono [Exchange: nexus.tenant.exchange] para: {}", tenantKey);
        try {
            TenantCreatedEvent event = new TenantCreatedEvent(tenantKey, serviceCode);
            rabbitTemplate.convertAndSend("nexus.tenant.exchange", "", event);
        } catch (Exception e) {
            // Un fallo en el Broker no debe romper la creación comercial del inquilino
            log.error("[ALERTA AMQP] El inquilino fue creado en CORE, pero el broker de mensajería no está disponible. El módulo HIS no será notificado en tiempo real. Causa: {}", e.getMessage());
        }
    }
}