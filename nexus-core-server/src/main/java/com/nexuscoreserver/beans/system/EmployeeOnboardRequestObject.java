package com.nexuscoreserver.beans.system;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Objeto de transferencia de datos (Bean de validación) para la petición de Onboarding
 * de empleados recibida en el Core Server.
 */
@Data
public class EmployeeOnboardRequestObject {
    
    private Boolean requiresSystemAccess; // Indica si se crearán credenciales de acceso en la base maestra

    private String username;
    private String password;
    private String roleName;
    
    private String tenantId; // Identificador del inquilino (obligatorio en el plano Core)

    // Datos clínicos / filiación (para persistir asíncronamente en el inquilino)
    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @NotBlank(message = "El apellido paterno es obligatorio")
    private String patname;

    @NotBlank(message = "El apellido materno es obligatorio")
    private String matname;

    @NotBlank(message = "El CURP es obligatorio")
    private String curp;

    @NotNull(message = "El identificador de departamento es obligatorio")
    private Long idDepartment;

    @NotNull(message = "El identificador de posición es obligatorio")
    private Long idPosition;

    @NotBlank(message = "El RFC es obligatorio")
    private String rfc;

    @NotBlank(message = "El género es obligatorio")
    private String gender;

    @NotBlank(message = "La fecha de nacimiento es obligatoria")
    private String datebirth;
}
