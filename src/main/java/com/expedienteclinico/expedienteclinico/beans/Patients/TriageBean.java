package com.expedienteclinico.expedienteclinico.beans.Patients;

import com.expedienteclinico.expedienteclinico.models.Patients.UrgencyLevel;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
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