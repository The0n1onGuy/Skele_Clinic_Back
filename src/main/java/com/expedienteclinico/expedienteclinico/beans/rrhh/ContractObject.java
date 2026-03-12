package com.expedienteclinico.expedienteclinico.beans.rrhh;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ContractObject {
    private Long id;
    private String uuid;

    private Long employeeId;
    private String employeeName;

    private String contractType;
    private String hiringDate;
    private String terminationDate;
    private BigDecimal baseSalary;

    private String statusName; // El nombre del estado (Activo, Inactivo, etc.)
}
