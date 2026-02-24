package com.expedienteclinico.expedienteclinico.repositories.Patients;

import com.expedienteclinico.expedienteclinico.models.Patients.AppointmentsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface IAppointmentsRepository extends JpaRepository <AppointmentsModel, Long>  {

}


