package com.nexusbusiness.listeners;

import com.nexusbusiness.config.PosTenantFlywayOrchestrator;
import com.nexussharedcore.events.TenantCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PosTenantEventListener {

    private final PosTenantFlywayOrchestrator posOrchestrator;

    @RabbitListener(queues = "pos.tenant.provisioning.queue")
    public void onTenantCreated(TenantCreatedEvent event) {
        log.info("<<< [RABBITMQ EVENTO RECIBIDO] Evaluando aprovisionamiento para inquilino: {}", event.getTenantKey());

        if ("POS".equalsIgnoreCase(event.getServiceCode())) {
            try {
                log.info(">>> Ejecutando motor de migración Flyway para el módulo POS...");
                posOrchestrator.migrateSingleTenant(event.getTenantKey());
            } catch (Exception e) {
                // Se captura la excepción y NO se re-lanza.
                // Esto "consume" el mensaje exitosamente para RabbitMQ, sacándolo de la cola.
                log.error("[ALERTA CRÍTICA] Fallo irrecuperable al aprovisionar {}. El mensaje ha sido descartado de la cola para evitar bucles. Requiere intervención manual. Detalle: {}", event.getTenantKey(), e.getMessage());
            }
        }
    }
}