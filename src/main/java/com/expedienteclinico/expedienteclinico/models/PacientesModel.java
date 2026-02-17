package com.expedienteclinico.expedienteclinico.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "pacientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PacientesModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String nombre;

    @Column(length = 50)
    private String apellido_materno;

    @Column(length = 50)
    private String apellido_paterno;

    @Column(unique = true, nullable = false)
    private String curp;

    private LocalDate fechaNacimiento;

    private String genero; // M, F, X

    private String telefono;
    private String email;
    private String direccion;
    private String tipoSangre;
}