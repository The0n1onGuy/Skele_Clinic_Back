package com.nexuscore.models.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HealthModel {
    private String status;         // UP, DOWN, UNSTABLE
    private String databaseStatus; // Connected, Disconnected
    private String internetStatus; // Stable, Unstable, Offline
    private long latencyMs;
    private LocalDateTime timestamp;
    private Map<String, Object> details; // Para info extra
}