package com.expedienteclinico.expedienteclinico.models.Patients;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "clinic_history")
@Data
public class ClinicHistory {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private PatientsModel paciente;

    @Column(nullable = false)
    private LocalDateTime fechaRegistro;

    @Column(length = 2000)
    private String motivoConsulta;

    @Column(length = 5000)
    private String enfermedadActual;

    private String diagnosticoPreliminar;

    // Relación con el médico que atendió (Ver punto 5)
    // private Long medicoId;
}