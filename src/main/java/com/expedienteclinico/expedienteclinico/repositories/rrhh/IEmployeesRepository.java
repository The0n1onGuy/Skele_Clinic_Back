package com.expedienteclinico.expedienteclinico.repositories.rrhh;
import com.expedienteclinico.expedienteclinico.models.rrhh.DepartmentsModel;
import com.expedienteclinico.expedienteclinico.models.rrhh.EmployeesModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IEmployeesRepository extends JpaRepository<EmployeesModel, Long> {
    boolean existsByNameIgnoreCase(String name);

    Optional<EmployeesModel> findByNameIgnoreCase(String name);
}
