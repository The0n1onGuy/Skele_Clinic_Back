package com.nexushiscore.repositories.rrhh;
import com.nexushiscore.models.rrhh.ClinicDetailsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IClinicDetailsRepository extends JpaRepository<ClinicDetailsModel, Long> {
//    boolean existsByNameIgnoreCase(String name);
//    boolean existsByProfessionalLicense(String professionalLicense);
//    Optional<ClinicDetailsModel> findByNameIgnoreCase(String name);
}
