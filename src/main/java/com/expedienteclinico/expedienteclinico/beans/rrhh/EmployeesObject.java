package com.expedienteclinico.expedienteclinico.beans.rrhh;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class EmployeesObject {
    private Long id;
    private UUID uuid;

    private String name;
    private String patname;
    private String matname;
    private String fullName;

    private String curp;
    private String rfc;
    private String datebirth;
    private String datereg;
    private String gender;

    private Long positionId;
    private String positionName;

    private Long departmentId;
    private String departmentName;

    private String statusName;
}
