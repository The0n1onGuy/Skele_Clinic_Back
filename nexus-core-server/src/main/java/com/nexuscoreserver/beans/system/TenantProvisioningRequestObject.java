package com.nexuscoreserver.beans.system;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TenantProvisioningRequestObject {

    @NotBlank(message = "El identificador del hospital es obligatorio")
    @Pattern(regexp = "^[a-z0-9_]+$", message = "El identificador solo puede contener letras minúsculas, números y guiones bajos")
    private String tenantKey; // ej: hospital_pediatrico

    @NotBlank(message = "Se requiere un identificador de servicio para dar de alta")
    private String serviceCode;

    @NotBlank(message = "El nombre comercial es obligatorio")
    private String displayName; // ej: Hospital General Pediátrico

    private String contactEmail;

    @NotBlank(message = "El usuario administrador es obligatorio")
    private String adminUsername; // ej: director_aurora

    @NotBlank(message = "La contraseña provisional es obligatoria")
    private String adminPassword; // ej: Temporal123!

    // Aquí se agregarán los booleanos de los módulos en la Fase 2
}