package com.nexushiscore.repositories.appointments;

import com.nexushiscore.models.appointments.AppointmentsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IAppointmentsRepository extends JpaRepository<AppointmentsModel, Long> {

    List<AppointmentsModel> findByPacienteNombre(String nombre);
}