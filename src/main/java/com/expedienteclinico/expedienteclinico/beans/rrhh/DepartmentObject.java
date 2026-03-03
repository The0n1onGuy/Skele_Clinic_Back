package com.expedienteclinico.expedienteclinico.beans.rrhh;

import lombok.Data;
import java.util.UUID;

@Data
public class DepartmentObject {
    private Long id;
    private String name;
    private UUID uuid;
    private String statusName;
}
