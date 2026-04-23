package com.nexuscore.beans.rrhh;
import java.time.LocalDate;
import lombok.Data;

@Data
public class EmployeeRequestObject {
    private String name;
    private String patname;
    private String matname;
    private String curp;
    private String rfc;
    private LocalDate datebirth;
    private LocalDate datereg;
    private String gender;

    // Plain text fields instead of IDs
    private String departmentName;
    private String positionName;
}