package com.nexushiscore.beans.rrhh;
import lombok.Data;

import java.time.LocalTime;
import java.util.UUID;

@Data
public class SchedulesObject {
    private Long id;
    private UUID uuid;

    private Long employeeId;
    private String employeeFullName;
//Campo para fechas: private LocalDateTime
    private String dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;

    private String statusName;
}
