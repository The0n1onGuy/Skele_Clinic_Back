package com.expedienteclinico.expedienteclinico.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "historial_clinico")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistorialesClinicosModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private PacientesModel paciente;

    @Column(nullable = false)
    private LocalDateTime fechaRegistro;

    @Column(length = 2000)
    private String motivoConsulta;

    @Column(length = 5000)
    private String enfermedadActual;

    private String diagnosticoPreliminar;
}
