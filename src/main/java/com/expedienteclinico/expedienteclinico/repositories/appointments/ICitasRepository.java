package com.expedienteclinico.expedienteclinico.repositories.appointments;

import com.expedienteclinico.expedienteclinico.models.appointments.CitasModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICitasRepository extends JpaRepository<CitasModel, Long> {
}