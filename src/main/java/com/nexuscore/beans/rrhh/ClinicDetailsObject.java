package com.nexuscore.beans.rrhh;
import lombok.Data;

@Data
public class ClinicDetailsObject {
    private Long id;
    private String uuid;

    private Long employeeId;
    private String employeeFullName;

    private String professionalLicense;
    private String graduationInstitution;
    private String specialty;

    private String statusName;
}
