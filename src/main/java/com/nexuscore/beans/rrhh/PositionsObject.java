package com.nexuscore.beans.rrhh;

import lombok.Data;
import java.util.UUID;

@Data
public class PositionsObject {
    private Long id;
    private UUID uuid;
    private String name;
    private String description;
    private String statusName;
}
