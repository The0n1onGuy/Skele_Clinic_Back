package com.expedienteclinico.expedienteclinico.repositories;

import com.expedienteclinico.expedienteclinico.models.Morgue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IMorgueRepository extends JpaRepository<Morgue, Long> {

    // n la tabla Morgue donde el campo paciente tenga el id que te mando
    Morgue findByPacienteId(Long id);
}