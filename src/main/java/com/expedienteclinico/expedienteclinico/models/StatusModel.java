package com.expedienteclinico.expedienteclinico.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Entity
@Table(name = "estatus") // Usamos name estándar de JPA
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // IDENTITY es mejor para MySQL/Postgres que SEQUENCE
    private Long id;

    @Column(nullable = false, unique = true)
    private String statusName; // Ej: "PENDIENTE", "CONFIRMADA", "EMERGENCIA_ROJA"

    // Opcional: Para distinguir si es estatus de cita o de triaje
    private String tipo; // Ej: "CITA", "TRIAJE"
}
