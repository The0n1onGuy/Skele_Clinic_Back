package com.expedienteclinico.expedienteclinico.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "triajes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TriajesModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private PacientesModel paciente;

    private LocalDateTime fechaHora;

    // Signos Vitales
    private Double temperatura;
    private String presionArterial;
    private Integer frecuenciaCardiaca;
    private Integer saturacionOxigeno;

    // APLICACIÓN DE STATUSMODEL
    // En lugar de Enum, relacionamos con la tabla de estatus
    @ManyToOne
    @JoinColumn(name = "nivel_urgencia_id")
    private StatusModel nivelUrgencia; // La BD tendrá registros como: "RESUCITACION", "EMERGENCIA"
}