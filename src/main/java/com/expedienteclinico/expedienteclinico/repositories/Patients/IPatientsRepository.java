package com.expedienteclinico.expedienteclinico.repositories.Patients;

import com.expedienteclinico.expedienteclinico.models.Patients.PatientsModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IPatientsRepository extends JpaRepository<PatientsModel, Long> {
    // Método mágico de Spring Data para buscar por CURP
    Optional<PatientsModel> findByCurp(String curp);
}
