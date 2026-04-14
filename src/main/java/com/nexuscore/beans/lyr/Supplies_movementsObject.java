package com.nexuscore.beans.lyr;

import lombok.Data;

@Data
public class Supplies_movementsObject{
    private Long id_motion;
    private String type_motion;
    private Integer amount;
    private String motion_date;
    private String observations;
    private String employeesName;
    private Long suppliesId;
}
