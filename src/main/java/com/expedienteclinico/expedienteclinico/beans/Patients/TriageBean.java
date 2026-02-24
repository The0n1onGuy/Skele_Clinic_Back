package com.expedienteclinico.expedienteclinico.beans.Patients;

import com.expedienteclinico.expedienteclinico.models.Patients.UrgencyLevel;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TriageBean {
    private Long id;
    private Long pacienteId;
    private LocalDateTime fechaHora;
    private Double temperatura;
    private String presionArterial;
    private Integer frecuenciaCardiaca;

    // 👇 Esta es la línea que probablemente falta o tiene un nombre diferente
    private Integer saturacionOxigeno;

    private UrgencyLevel nivel;
}