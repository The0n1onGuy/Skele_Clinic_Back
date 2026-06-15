package com.nexussharedcore.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

/**
 * Evento común que representa la creación exitosa de un inquilino.
 * Modificado para incluir 'eventId' que actúa como clave de idempotencia
 * en los microservicios de dominio (HIS / POS).
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TenantCreatedEvent implements Serializable {
    private String eventId; // Identificador único determinista para control de idempotencia
    private String tenantKey; // Clave única del inquilino (nombre del esquema físico)
    private String serviceCode; // Código del servicio contratado (ej: "HIS", "POS")
}