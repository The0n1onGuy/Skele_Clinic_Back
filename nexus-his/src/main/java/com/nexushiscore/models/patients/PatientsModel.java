package com.nexushiscore.models.patients;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "patient")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class PatientsModel {
    @Id
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private UUID id;  // ← CAMBIO: Long → UUID

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
    // CAMPOS AÑADIDOS PARA ALINEACIÓN CLÍNICA
    @Column(name = "contacto_emergencia")
    private String contactoEmergencia;

    @Column(name = "alergias", columnDefinition = "TEXT")
    private String alergias;

    @Column(name = "enfermedades_cronicas", columnDefinition = "TEXT")
    private String enfermedadesCronicas;

    // Relación con el Historial (Uno a muchos)
    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ClinicHistoryModel> historiales;
}