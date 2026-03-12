package com.expedienteclinico.expedienteclinico.beans.rrhh;
import lombok.Data;
import java.util.UUID;

@Data
public class SchedulesObject {
    private Long id;
    private UUID uuid;

    private Long employeeId;
    private String employeeFullName;

    private String dayOfWeek;
    private String startTime;
    private String endTime;

    private String statusName;
}
