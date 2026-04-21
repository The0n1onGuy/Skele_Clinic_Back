package com.expedienteclinico.expedienteclinico.models.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class HealthModelTest {

    @Test
    @DisplayName("Deberia crear una instancia de HealthModel y validar sus campos")
    void testHealthModelCreation() {
        LocalDateTime now = LocalDateTime.now();
        HealthModel health = new HealthModel("UP", "Connected", "Stable", 45L, now, null);

        assertAll("Verificacion de atributos de salud",
                () -> assertEquals("UP", health.getStatus()),
                () -> assertEquals("Connected", health.getDatabaseStatus()),
                () -> assertEquals(45L, health.getLatencyMs()),
                () -> assertEquals(now, health.getTimestamp())
        );
    }
}