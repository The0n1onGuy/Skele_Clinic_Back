package com.nexussharedcore.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

/**
 * Evento de compensación SAGA que representa el fallo al dar de alta el expediente clínico local.
 * Emitido por el HIS ante errores de integridad del inquilino y consumido por el Core
 * para revertir o desactivar el usuario de sistema creado de manera transaccional.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserOnboardFailedEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    private String eventId; // UUID original del evento de onboarding para trazabilidad de la SAGA
    private String tenantId; // Inquilino donde ocurrió el fallo de persistencia
    private String username; // Nombre de usuario en master que requiere rollback
    private String errorMessage; // Detalle de la falla de negocio en el inquilino
}
