package com.nexuscore.beans.rrhh;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmployeeOnboardingRequestObject {
    // --- Bander condicional, decide si el empleado a registrar requiere credenciales en el sistema
    private Boolean requiresSystemAccess;

    private String username;
    private String password;
    private String roleName;

    // --- OPERACIÓN (Tenant) ---
    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @NotBlank(message = "El apellido paterno es obligatorio")
    private String patname;
    @NotBlank(message = "El apellido paterno es obligatorio")
    private String matname;

    @NotBlank(message = "El CURP es obligatorio")
    private String curp;

    // Asumiremos estos IDs referencian a los catálogos rrhh_departments y rrhh_positions
    private Long idDepartment;
    private Long idPosition;
    private String rfc;
    private String gender;
    private String datebirth;
}