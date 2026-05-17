package com.nexushiscore.repositories.Patients;

import com.nexushiscore.models.patients.ClinicHistoryModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IClinicHistoryRepository  extends JpaRepository<ClinicHistoryModel, UUID> {

}
