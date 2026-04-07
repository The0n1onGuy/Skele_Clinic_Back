package com.expedienteclinico.expedienteclinico.repositories.rrhh;
import com.expedienteclinico.expedienteclinico.beans.rrhh.ClinicDetailsObject;
import com.expedienteclinico.expedienteclinico.models.rrhh.ClinicDetailsModel;
import com.expedienteclinico.expedienteclinico.models.rrhh.DepartmentsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface IClinicDetailsRepository extends JpaRepository<ClinicDetailsModel, Long> {
//    boolean existsByNameIgnoreCase(String name);
//    boolean existsByProfessionalLicense(String professionalLicense);
//    Optional<ClinicDetailsModel> findByNameIgnoreCase(String name);
}
