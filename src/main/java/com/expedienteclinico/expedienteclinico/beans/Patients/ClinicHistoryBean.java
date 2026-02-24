package com.expedienteclinico.expedienteclinico.beans.Patients;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ClinicHistoryBean {
    private UUID pacienteId;  // Solo pedimos el ID del paciente
    private LocalDateTime fechaRegistro;
    private String motivoConsulta;
    private String enfermedadActual;
    private String diagnosticoPreliminar;
}