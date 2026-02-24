package com.expedienteclinico.expedienteclinico.models.Patients;

import lombok.AllArgsConstructor;
import lombok.Data; // Incluye Getter, Setter, toString, etc.
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "patient")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class PatientsModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellidos;

    @Column(unique = true, nullable = false)
    private String curp; // O DNI/Cédula

    private LocalDate fechaNacimiento;

    private String genero; // M, F, X

    private String telefono;
    private String email;
    private String direccion;
    private String tipoSangre;

    // Relación con el Historial (Uno a muchos)
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ClinicHistoryModel> historiales;
}