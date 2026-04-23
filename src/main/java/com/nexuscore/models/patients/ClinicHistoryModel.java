package com.nexuscore.models.patients;

import lombok.Data;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "clinic_history")
@Data
public class ClinicHistoryModel {
    @Id
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private UUID id;  // ← CAMBIO: Long → UUID

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private PatientsModel paciente;

    @Column(nullable = false)
    private LocalDateTime fechaRegistro;

    @Column(length = 2000)
    private String motivoConsulta;

    @Column(length = 5000)
    private String padecimientoActual;

    private String diagnosticoPreliminar;

    // Relación con el médico que atendió (Ver punto 5)
    // private Long medicoId;
}