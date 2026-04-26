package com.nexushiscore.repositories.Patients;

import com.nexushiscore.models.patients.PatientsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IPatientsRepository extends JpaRepository<PatientsModel, UUID> {
}
