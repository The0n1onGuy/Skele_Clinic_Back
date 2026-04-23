package com.nexuscore.beans.cleaningandclothing;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Cleaning_suppliesObject {

    private String uuid;
    private Long suppliesId;

    private String name;
    private String description;

    private String unitMeasurement;
    private Integer stockMin;
    private Integer currentStock;

    private LocalDate expirationDate;

    private String statusUuid;
    private String statusName;
}
