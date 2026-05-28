package com.nexusbusiness.listeners;

import com.nexusbusiness.config.PosTenantFlywayOrchestrator;
import com.nexussharedcore.events.TenantCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Listener de eventos AMQP para el aprovisionamiento de negocios en el módulo POS.
 * Implementa el patrón "Idempotent Consumer" mediante la tabla local 'processed_events'
 * para evitar ejecuciones duplicadas de Flyway ante fallas o re-entregas en la red.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PosTenantEventListener {

    private final PosTenantFlywayOrchestrator posOrchestrator;
    private final JdbcTemplate jdbcTemplate;

    @RabbitListener(queues = "pos.tenant.provisioning.queue")
    public void onTenantCreated(TenantCreatedEvent event) {
        log.info("<<< [POS LISTENER] Recibido evento de aprovisionamiento. Inquilino: {}, EventId: {}", 
                event.getTenantKey(), event.getEventId());

        // Validamos si este evento está dirigido al módulo de negocio (POS)
        if ("POS".equalsIgnoreCase(event.getServiceCode())) {
            String tenantKey = event.getTenantKey();
            String eventId = event.getEventId();

            try {
                // Validación contra inyección SQL
                validateTenantKey(tenantKey);

                // 1. Verificación de Idempotencia:
                // Comprobamos si la tabla 'processed_events' existe en el esquema del inquilino y si ya registró este eventId
                if (isEventAlreadyProcessed(tenantKey, eventId)) {
                    log.info("<<< [IDEMPOTENCIA ACTIVADA] El evento {} ya fue procesado para el inquilino {}. Saltando migración.", 
                            eventId, tenantKey);
                    return; // Retorno inmediato (idempotente)
                }

                // 2. Ejecutar la migración física (Materializar el esquema POS mediante Flyway)
                log.info(">>> Ejecutando motor de migración Flyway para el módulo POS en: {}", tenantKey);
                posOrchestrator.migrateSingleTenant(tenantKey);

                // 3. Registrar el evento como procesado para asegurar la idempotencia futura
                registerEventProcessed(tenantKey, eventId, "TenantCreatedEvent");
                log.info("<<< [IDEMPOTENCIA REGISTRADA] Evento {} marcado como procesado en la base de datos del inquilino.", eventId);

            } catch (Exception e) {
                // Capturamos y registramos el error sin re-lanzar para consumir el mensaje y evitar bucles infinitos en RabbitMQ
                log.error("[ALERTA CRÍTICA POS] Fallo irrecuperable al aprovisionar {}. El mensaje ha sido descartado de la cola para evitar bucles. Requiere intervención manual. Detalle: {}", 
                        tenantKey, e.getMessage());
            }
        }
    }

    /**
     * Comprueba si la tabla 'processed_events' existe y contiene el identificador de evento especificado.
     * Consulta directamente el diccionario de información y el esquema dinámico del inquilino de forma segura.
     */
    private boolean isEventAlreadyProcessed(String tenantKey, String eventId) {
        validateTenantKey(tenantKey);
        try {
            // Validamos primero en el diccionario de información de MySQL si la tabla física ya ha sido creada
            String checkTableSql = "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = ? AND table_name = 'processed_events'";
            Integer tableExists = jdbcTemplate.queryForObject(checkTableSql, Integer.class, tenantKey);

            if (tableExists != null && tableExists > 0) {
                // Si la tabla de resiliencia ya existe, consultamos si ya registra este ID de evento específico
                String checkEventSql = "SELECT COUNT(*) FROM " + tenantKey + ".processed_events WHERE event_id = ?";
                Integer eventCount = jdbcTemplate.queryForObject(checkEventSql, Integer.class, eventId);
                return eventCount != null && eventCount > 0;
            }
        } catch (Exception e) {
            log.warn("No se pudo verificar la idempotencia en el esquema {} (es normal en la primera instalación). Causa: {}", tenantKey, e.getMessage());
        }
        return false;
    }

    /**
     * Registra el ID de evento procesado en la tabla de idempotencia local de la base de datos del inquilino.
     */
    private void registerEventProcessed(String tenantKey, String eventId, String eventType) {
        validateTenantKey(tenantKey);
        try {
            String insertEventSql = "INSERT INTO " + tenantKey + ".processed_events (event_id, event_type) VALUES (?, ?)";
            jdbcTemplate.update(insertEventSql, eventId, eventType);
        } catch (Exception e) {
            log.error("Fallo al registrar la idempotencia del evento {} en el esquema {}: {}", eventId, tenantKey, e.getMessage());
        }
    }

    /**
     * Sanitiza y valida de manera estricta el identificador de inquilino (Tenant) para prevenir
     * cualquier vector de ataque por Inyección SQL en consultas de esquemas dinámicos.
     */
    private void validateTenantKey(String tenantKey) {
        if (tenantKey == null || !tenantKey.matches("^[a-z0-9_-]{3,64}$")) {
            throw new IllegalArgumentException("Identificador de inquilino malicioso o inválido detectado: " + tenantKey);
        }
    }
}