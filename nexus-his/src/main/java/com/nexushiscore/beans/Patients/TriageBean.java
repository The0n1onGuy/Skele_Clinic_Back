package com.nexushiscore.beans.Patients;

import com.nexushiscore.models.patients.UrgencyLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class TriageBean {
    private UUID pacienteId;
    private LocalDateTime fechaHora;
    private Double temperatura;
    private String presionArterial;
    private Integer frecuenciaCardiaca;

    // 👇 Esta es la línea que probablemente falta o tiene un nombre diferente
    private Integer saturacionOxigeno;

    private UrgencyLevel nivel;
}