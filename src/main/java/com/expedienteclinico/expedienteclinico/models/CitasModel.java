package com.expedienteclinico.expedienteclinico.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "citas")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class CitasModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String pacienteNombre;
    private String especialista;
    private LocalDate fecha;
    private LocalTime hora;
    private String motivo;
    private String estado;
}