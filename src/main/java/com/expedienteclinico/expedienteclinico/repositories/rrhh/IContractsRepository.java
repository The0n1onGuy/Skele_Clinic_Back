package com.expedienteclinico.expedienteclinico.repositories.rrhh;
import com.expedienteclinico.expedienteclinico.models.rrhh.ContractsModel;
import com.expedienteclinico.expedienteclinico.models.rrhh.EmployeesModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface IContractsRepository extends JpaRepository<ContractsModel, Long> {
//    boolean existsByNameIgnoreCase(String name);
//
//    Optional<ContractsModel> findByNameIgnoreCase(String name);
}
