package com.nexuscoreserver.services.system;

import com.nexuscoreserver.beans.system.TenantProvisioningRequestObject;
import com.nexussharedcore.events.TenantCreatedEvent;
import com.nexussharedcore.repositories.common.IOutboxMessageRepository;
import com.nexussharedcore.models.common.OutboxMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.scheduling.annotation.Scheduled;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Servicio central para el aprovisionamiento físico y lógico de nuevos inquilinos (Tenants) en la plataforma.
 * Diseñado con el Patrón Outbox Transaccional y Orquestación SAGA Coreografiada para asegurar atomicidad
 * en la creación de recursos y consistencia eventual multi-servicio en RabbitMQ.
 */
@Slf4j
@Service
public class TenantProvisioningService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DataSource masterDataSource;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private IOutboxMessageRepository outboxRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Orquestador principal para dar de alta un inquilino.
     * DDL (CREATE DATABASE) no soporta transacciones en MySQL (provoca un commit implícito).
     * Por ello, este método corre sin contexto transaccional nativo, pero delega las escrituras
     * lógicas a un método transaccional dedicado.
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void provisionNewTenant(TenantProvisioningRequestObject request) {
        String tenantKey = request.getTenantKey();
        log.info("=== STARTING SAAS PROVISIONING FOR TENANT: {} ===", tenantKey);

        // FASE 1: Creación Física del Esquema de Base de Datos del Inquilino (DDL Directo)
        createPhysicalDatabase(tenantKey);

        // Generamos un eventId UUID único y definimos el enrutamiento dinámico según el servicio contratado
        String eventId = UUID.randomUUID().toString();
        
        // El enrutamiento dinámico resuelve la segregación del multi-SaaS:
        // Si el hospital contrata HIS, el evento se envía únicamente a la cola HIS usando la routingKey correspondiente.
        String routingKey = "tenant.provision." + request.getServiceCode().toLowerCase();

        // FASE 2, 3 & 4 (Efectuadas atómicamente dentro de una sola transacción local)
        registerTenantMetadataAndOutbox(request, eventId, routingKey);

        log.info("=== TENANT METADATA AND OUTBOX PERSISTED FOR {} [eventId: {}] ===", tenantKey, eventId);
    }

    /**
     * Ejecuta la creación del esquema físico en MySQL de forma directa y controlada.
     */
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

    /**
     * Registra de forma estrictamente transaccional (ACID) la información del inquilino,
     * su suscripción SaaS, las credenciales del Administrador e inyecta la entrada en la tabla Outbox.
     * Si cualquiera de estas operaciones falla, todo se revierte (incluso el mensaje a despachar).
     */
    @Transactional
    public void registerTenantMetadataAndOutbox(TenantProvisioningRequestObject request, String eventId, String routingKey) {
        log.info("-> Phase 2, 3 & 4: Atomic write to System Directory & Outbox Table...");
        JdbcTemplate masterJdbcTemplate = new JdbcTemplate(masterDataSource);

        try {
            // 1. Inserción del Inquilino en el Directorio Master
            String sqlTenant = "INSERT INTO system_tenants (status_id, tenant_key, display_name, contact_email) " +
                    "VALUES ((SELECT id_status FROM status WHERE name = 'Active' LIMIT 1), ?, ?, ?)";
            masterJdbcTemplate.update(sqlTenant, request.getTenantKey(), request.getDisplayName(), request.getContactEmail());

            // 2. Inserción de la Suscripción al Módulo SaaS
            String sqlSubscription = "INSERT INTO tenant_subscriptions (id_tenant, id_service, status_id) " +
                    "VALUES (" +
                    "(SELECT id_tenant FROM system_tenants WHERE tenant_key = ? LIMIT 1), " +
                    "(SELECT id_service FROM system_services WHERE service_code = ? LIMIT 1), " +
                    "(SELECT id_status FROM status WHERE name = 'Active' LIMIT 1))";
            masterJdbcTemplate.update(sqlSubscription, request.getTenantKey(), request.getServiceCode());

            // 3. Creación de las credenciales de Acceso Administrador para el Inquilino
            String hashedPassword = passwordEncoder.encode(request.getAdminPassword());
            String newUuid = UUID.randomUUID().toString();
            String sqlUser = "INSERT INTO system_users (role_id, status_id, uuid, tenant_id, user_name, password, is_2fa_enabled) " +
                    "VALUES (" +
                    "(SELECT id_role FROM system_roles WHERE role_name = 'ROLE_ADMIN' LIMIT 1), " +
                    "(SELECT id_status FROM status WHERE name = 'Active' LIMIT 1), " +
                    "?, ?, ?, ?, false)";
            masterJdbcTemplate.update(sqlUser, newUuid, request.getTenantKey(), request.getAdminUsername(), hashedPassword);

            // 4. Inserción Transaccional en el Outbox (Evita el Dual-Write problem)
            TenantCreatedEvent event = new TenantCreatedEvent(eventId, request.getTenantKey(), request.getServiceCode());
            String jsonPayload = objectMapper.writeValueAsString(event);

            OutboxMessage outboxMessage = OutboxMessage.builder()
                    .id(eventId)
                    .aggregateType("TENANT")
                    .aggregateId(request.getTenantKey())
                    .eventType("TenantCreatedEvent")
                    .payload(jsonPayload)
                    .routingKey(routingKey)
                    .status("PENDING")
                    .createdAt(LocalDateTime.now())
                    .build();

            outboxRepository.save(outboxMessage);
            log.info("-> Transactional Outbox message registered as PENDING for UUID: {}", eventId);

        } catch (Exception e) {
            throw new RuntimeException("Failed to atomically register tenant metadata or write Outbox entry.", e);
        }
    }

    /**
     * Scheduler periódico de background (Outbox Publisher / Message Relay).
     * Revisa de forma asíncrona la tabla physical de outbox cada 5 segundos buscando mensajes 'PENDING'.
     * Los despacha quirúrgicamente usando TopicExchange y confirma la entrega de forma transaccional local.
     */
    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void dispatchOutboxMessages() {
        List<OutboxMessage> pendingMessages = outboxRepository.findByStatus("PENDING");
        if (pendingMessages.isEmpty()) {
            return;
        }

        log.info("-> Outbox Dispatcher: Processing {} pending outbox events...", pendingMessages.size());

        for (OutboxMessage msg : pendingMessages) {
            try {
                // Deserializamos dinámicamente el payload según el tipo de evento registrado
                Object event;
                if ("TenantCreatedEvent".equalsIgnoreCase(msg.getEventType())) {
                    event = objectMapper.readValue(msg.getPayload(), TenantCreatedEvent.class);
                } else if ("UserOnboardRequestedEvent".equalsIgnoreCase(msg.getEventType())) {
                    event = objectMapper.readValue(msg.getPayload(), com.nexussharedcore.events.UserOnboardRequestedEvent.class);
                } else {
                    log.warn("-> Outbox Dispatcher: Tipo de evento desconocido o no soportado: {}", msg.getEventType());
                    continue;
                }

                // Despacho asíncrono seguro a RabbitMQ con su clave de enrutamiento Topic dedicada
                rabbitTemplate.convertAndSend("nexus.tenant.exchange", msg.getRoutingKey(), event);

                // Confirmamos exitosamente marcando el registro local como SENT
                msg.setStatus("SENT");
                msg.setProcessedAt(LocalDateTime.now());
                outboxRepository.save(msg);

                log.info("-> Outbox message successfully dispatched and updated to SENT for ID: {}", msg.getId());
            } catch (Exception e) {
                log.error("[OUTBOX DISPATCH FAILURE] Failed to publish message {}. It will remain PENDING for retry. Causa: {}", msg.getId(), e.getMessage());
            }
        }
    }
}