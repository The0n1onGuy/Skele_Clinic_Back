package com.expedienteclinico.expedienteclinico.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "citas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CitasModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación necesaria con Paciente (aunque no estaba en tu foto, es vital)
    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private PacientesModel paciente;

    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;

    private String consultorio;

    // APLICACIÓN DE STATUSMODEL
    // En lugar de Enum, relacionamos con la tabla de estatus
    @ManyToOne
    @JoinColumn(name = "estatus_cita_id")
    private StatusModel estatus; // La BD tendrá registros como: "PENDIENTE", "CANCELADA"
}
