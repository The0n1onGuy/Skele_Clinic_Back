package com.nexussharedcore.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

/**
 * Evento de integración que representa la petición de Onboarding de un empleado.
 * Generado por el Core Plane de manera transaccional local (Outbox) y consumido
 * por el listener clínico del HIS de forma asíncrona e idempotente.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserOnboardRequestedEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    private String eventId; // Identificador único determinista (UUID) para resiliencia e idempotencia
    private String tenantId; // Identificador del inquilino (esquema destino en MySQL)
    private Boolean requiresSystemAccess; // Indica si el empleado requiere usuario en la base maestra

    // Credenciales (Fijadas en el Core)
    private String username;
    private String password;
    private String roleName;

    // Filiación y Datos Clínicos (Persistidos en el Inquilino)
    private String name;
    private String patname;
    private String matname;
    private String curp;
    private String rfc;
    private String gender;
    private String datebirth;
    private Long idDepartment;
    private Long idPosition;
}
