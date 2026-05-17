package com.nexussharedcore.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TenantCreatedEvent implements Serializable {
    private String tenantKey;
    private String serviceCode;
}