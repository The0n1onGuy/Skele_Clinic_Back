package com.expedienteclinico.expedienteclinico.beans.Patients;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ClinicHistoryBean {
    private Long id;
    private Long pacienteId; // Solo pedimos el ID del paciente
    private LocalDateTime fechaRegistro;
    private String motivoConsulta;
    private String enfermedadActual;
    private String diagnosticoPreliminar;
}