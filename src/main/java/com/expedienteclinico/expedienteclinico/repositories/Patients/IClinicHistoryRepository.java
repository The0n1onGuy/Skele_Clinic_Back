package com.expedienteclinico.expedienteclinico.repositories.Patients;

import com.expedienteclinico.expedienteclinico.models.Patients.ClinicHistoryModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IClinicHistoryRepository  extends JpaRepository<ClinicHistoryModel, Long> {

}
