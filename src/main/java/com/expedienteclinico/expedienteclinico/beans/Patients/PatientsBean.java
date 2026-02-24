package com.expedienteclinico.expedienteclinico.beans.Patients;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientsBean {
    private UUID pacienteId;            // ← CAMBIO: Long → UUID
    private String nombre;
    private String apellidos;
    private String curp;
    private LocalDate fechaNacimiento;
    private String genero;
    private String telefono;
    private String email;
    private String direccion;
    private String tipoSangre;
}