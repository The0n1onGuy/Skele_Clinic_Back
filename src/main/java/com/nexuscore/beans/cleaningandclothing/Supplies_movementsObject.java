package com.nexuscore.beans.cleaningandclothing;

import lombok.Data;

@Data
public class Supplies_movementsObject{
    private String uuid;
    private Long Id_motion;
    private String movementType;
    private Integer amount;
    private String movementDate;
    private String observations;
    private String employeesName;
    private Long supply;
}
