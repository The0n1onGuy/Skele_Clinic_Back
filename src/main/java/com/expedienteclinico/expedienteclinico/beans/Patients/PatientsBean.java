package com.expedienteclinico.expedienteclinico.beans.Patients;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

    @Data // ¡Esto te crea todos los getters y setters automáticamente!
    @NoArgsConstructor
    @AllArgsConstructor
    public class PatientsBean {

        // El ID es útil cuando devuelves la lista de pacientes al frontend
        private Long id;

        // Mismos campos que el modelo, pero sin las anotaciones de base de datos (@Column, @Entity, etc.)
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

