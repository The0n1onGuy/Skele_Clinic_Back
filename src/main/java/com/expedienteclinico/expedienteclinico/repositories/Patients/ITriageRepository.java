package com.expedienteclinico.expedienteclinico.repositories.Patients;

import com.expedienteclinico.expedienteclinico.models.Patients.TriageModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITriageRepository  extends JpaRepository<TriageModel, Long> {

}