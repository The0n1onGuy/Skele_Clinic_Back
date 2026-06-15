package com.nexussharedcore.models.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad de dominio común para el Patrón Outbox.
 * Mapea la tabla 'outbox_messages' para registrar de manera transaccional y atómica
 * los eventos de negocio que deben despacharse al middleware de mensajería (RabbitMQ).
 */
@Entity
@Table(name = "outbox_messages")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutboxMessage {

    @Id
    @Column(name = "id", length = 36)
    private String id; // UUID determinista del evento para evitar colisiones y asegurar idempotencia

    @Column(name = "aggregate_type", nullable = false)
    private String aggregateType; // Tipo de agregado que originó el evento (ej: "TENANT")

    @Column(name = "aggregate_id", nullable = false)
    private String aggregateId; // Identificador de la entidad agregada (ej: tenantKey)

    @Column(name = "event_type", nullable = false)
    private String eventType; // Nombre técnico del evento para su correcta deserialización (ej: "TenantCreatedEvent")

    @Column(name = "payload", columnDefinition = "JSON", nullable = false)
    private String payload; // Representación serializada del payload del evento en JSON

    @Column(name = "routing_key", nullable = false)
    private String routingKey; // Clave de enrutamiento para RabbitMQ (ej: tenant.provision.his, tenant.provision.pos)

    @Column(name = "status", nullable = false, length = 50)
    private String status; // Estado de envío del mensaje (PENDING, SENT, FAILED)

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt; // Fecha de registro en base de datos de origen

    @Column(name = "processed_at")
    private LocalDateTime processedAt; // Fecha en la que se confirmó el envío hacia RabbitMQ
}
