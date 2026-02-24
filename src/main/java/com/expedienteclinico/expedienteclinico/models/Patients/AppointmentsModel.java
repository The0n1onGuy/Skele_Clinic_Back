package com.expedienteclinico.expedienteclinico.models.Patients;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments") // Ya no marcará error al borrar el import de Hibernate
@Data
@NoArgsConstructor // Obligatorio para JPA
@AllArgsConstructor
public class AppointmentsModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private PatientsModel paciente; // Ojo: Asegúrate de que tu modelo se llame PatientsModel y no PacienteModel

    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;

    @Enumerated(EnumType.STRING)
    private StatusAppointment estado;

    private String consultorio;
}