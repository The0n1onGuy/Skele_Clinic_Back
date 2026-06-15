package com.nexushiscore.listeners;

import com.nexushiscore.config.HisTenantFlywayOrchestrator;
import com.nexussharedcore.events.TenantCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Listener de eventos AMQP para el aprovisionamiento clínico en el módulo HIS.
 * Implementa el patrón "Idempotent Consumer" mediante la tabla local 'processed_events'
 * para evitar ejecuciones duplicadas de Flyway ante fallas o re-entregas en la red.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class HisTenantEventListener {

    private final HisTenantFlywayOrchestrator hisOrchestrator;
    private final JdbcTemplate jdbcTemplate;
    
    // Inyección de dependencias para el Onboarding asíncrono
    private final com.nexushiscore.repositories.rrhh.IEmployeesRepository employeesRepository;
    private final com.nexushiscore.repositories.rrhh.IPositionsRepository positionsRepository;
    private final com.nexushiscore.repositories.rrhh.IDepartmentsRepository departmentsRepository;
    private final com.nexushiscore.repositories.system.IStatusRepository statusRepository;
    private final org.springframework.amqp.rabbit.core.RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "his.tenant.provisioning.queue")
    public void onTenantCreated(TenantCreatedEvent event) {
        log.info("<<< [HIS LISTENER] Recibido evento de aprovisionamiento. Inquilino: {}, EventId: {}", 
                event.getTenantKey(), event.getEventId());

        // Validamos si este evento está dirigido al módulo clínico (HIS)
        if ("HIS".equalsIgnoreCase(event.getServiceCode())) {
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

                // 2. Ejecutar la migración física (Materializar el esquema clínico mediante Flyway)
                log.info(">>> Ejecutando motor de migración Flyway para el módulo HIS en: {}", tenantKey);
                hisOrchestrator.migrateSingleTenant(tenantKey);

                // 3. Registrar el evento como procesado para asegurar la idempotencia futura
                registerEventProcessed(tenantKey, eventId, "TenantCreatedEvent");
                log.info("<<< [IDEMPOTENCIA REGISTRADA] Evento {} marcado como procesado en la base de datos del inquilino.", eventId);

            } catch (Exception e) {
                // Capturamos y registramos el error sin re-lanzar para consumir el mensaje y evitar bucles infinitos en RabbitMQ
                log.error("[ALERTA CRÍTICA HIS] Fallo irrecuperable al aprovisionar {}. El mensaje ha sido descartado de la cola para evitar bucles. Requiere intervención manual. Detalle: {}", 
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

    /**
     * Listener asíncrono para el Onboarding clínico de empleados.
     * Consume la petición desde RabbitMQ, valida la idempotencia en la base del inquilino,
     * persiste el expediente local y emite un evento de compensación (Rollback) si ocurre algún error.
     */
    @RabbitListener(queues = "his.user.onboard.queue")
    public void onUserOnboardRequested(com.nexussharedcore.events.UserOnboardRequestedEvent event) {
        log.info("<<< [HIS LISTENER] Recibido evento de Onboarding de Empleado. Curp: {}, EventId: {}", 
                event.getCurp(), event.getEventId());

        String tenantId = event.getTenantId();
        String eventId = event.getEventId();
        
        try {
            // Aislamiento y validación de seguridad contra inyección SQL
            validateTenantKey(tenantId);
            
            // 1. Aislamiento dinámico del inquilino en Hibernate/MySQL
            com.nexussharedcore.security.TenantContext.setCurrentTenant(tenantId);

            // 2. Verificación de Idempotencia local
            if (isEventAlreadyProcessed(tenantId, eventId)) {
                log.info("<<< [IDEMPOTENCIA ACTIVADA] El evento {} ya fue procesado para el inquilino {}. Saltando inserción.", 
                        eventId, tenantId);
                return;
            }

            // 2.5 Verificación de integridad de negocio (Duplicidad de CURP) para disparar SAGA Rollback
            if (employeesRepository.existsBycurp(event.getCurp())) {
                throw new RuntimeException("El CURP '" + event.getCurp() + "' ya está registrado en el inquilino clínico: " + tenantId);
            }

            // 3. Persistencia local en la base del inquilino usando los Repositorios JPA correspondientes
            log.info(">>> Guardando perfil clínico local del empleado '{}' en el tenant '{}'...", event.getName(), tenantId);
            
            com.nexushiscore.models.rrhh.EmployeesModel employee = new com.nexushiscore.models.rrhh.EmployeesModel();
            employee.setUuid(java.util.UUID.fromString(eventId)); // Usamos el eventId como UUID para consistencia distribuida
            employee.setName(event.getName());
            employee.setPatname(event.getPatname());
            employee.setMatname(event.getMatname() != null ? event.getMatname() : "");
            employee.setCurp(event.getCurp());
            employee.setRfc(event.getRfc());
            employee.setGender(event.getGender());
            employee.setDatebirth(java.time.LocalDate.parse(event.getDatebirth()));
            employee.setDatereg(java.time.LocalDate.now());

            com.nexushiscore.models.rrhh.PositionsModel position = positionsRepository.findById(event.getIdPosition())
                    .orElseThrow(() -> new RuntimeException("Posición no encontrada: " + event.getIdPosition()));
            employee.setId_position(position);

            com.nexushiscore.models.rrhh.DepartmentsModel department = departmentsRepository.findById(event.getIdDepartment())
                    .orElseThrow(() -> new RuntimeException("Departamento no encontrado: " + event.getIdDepartment()));
            employee.setId_department(department);

            com.nexushiscore.models.system.StatusModel activeStatus = statusRepository.findByStatusNameIgnoreCase("Active")
                    .orElseThrow(() -> new RuntimeException("Estado 'Active' no encontrado en el catálogo del inquilino."));
            employee.setId_status(activeStatus);

            // Guardar y forzar flush inmediato para disparar cualquier restricción de integridad (ej: CURP único) dentro del bloque try-catch
            employeesRepository.saveAndFlush(employee);

            // 4. Registrar evento procesado para asegurar la idempotencia futura
            registerEventProcessed(tenantId, eventId, "UserOnboardRequestedEvent");
            log.info("<<< [SAGA HIS EXITO] Expediente clínico del empleado registrado e idempotencia guardada.");

        } catch (Exception e) {
            log.error("[ALERTA SAGA HIS] Error al persistir el expediente local para CURP {}. Detalle: {}", event.getCurp(), e.getMessage());
            
            // 5. ACCIÓN DE COMPENSACIÓN SAGA: Emitir evento de fallo de vuelta al Core Server si requiere acceso de sistema
            if (event.getRequiresSystemAccess() != null && event.getRequiresSystemAccess()) {
                log.warn(">>> Emitiendo evento de compensación SAGA hacia el Core para revertir credenciales de '{}'", event.getUsername());
                try {
                    com.nexussharedcore.events.UserOnboardFailedEvent failedEvent = new com.nexussharedcore.events.UserOnboardFailedEvent(
                            eventId,
                            tenantId,
                            event.getUsername(),
                            e.getMessage()
                    );
                    // Publicamos al canal de fallos con la routing key de compensación 'user.onboard.failed'
                    rabbitTemplate.convertAndSend("nexus.tenant.exchange", "user.onboard.failed", failedEvent);
                } catch (Exception ex) {
                    log.error("[FALLO CRITICO COMPENSACIÓN] No se pudo enviar el evento de rollback al Core: {}", ex.getMessage());
                }
            }
        } finally {
            // Limpieza del contexto
            com.nexussharedcore.security.TenantContext.clear();
        }
    }
}