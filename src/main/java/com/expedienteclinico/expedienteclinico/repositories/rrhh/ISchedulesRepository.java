package com.expedienteclinico.expedienteclinico.repositories.rrhh;
import com.expedienteclinico.expedienteclinico.models.rrhh.DepartmentsModel;
import com.expedienteclinico.expedienteclinico.models.rrhh.SchedulesModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ISchedulesRepository extends JpaRepository<SchedulesModel, Long> {
//    boolean existsOverlap(String name);
//
//    Optional<SchedulesModel> findByNameIgnoreCase(String name);
}
