package com.nexushiscore.beans.rrhh;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "The employee department is required")
    private Long idDepartment;

    @NotNull(message = "The employee position is required")
    private Long idPosition;

    @NotBlank(message = "The RFC is required")
    private String rfc;

    @NotBlank(message = "The gender is required")
    private String gender;

    @NotBlank(message = "The date of birth is required")
    private String datebirth;
}