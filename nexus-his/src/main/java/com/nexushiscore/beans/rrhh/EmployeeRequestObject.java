package com.nexushiscore.beans.rrhh;

import lombok.Data;

import java.time.LocalDate;

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