package com.nexuscore.repositories.rrhh;
import com.nexuscore.models.rrhh.SchedulesModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISchedulesRepository extends JpaRepository<SchedulesModel, Long> {
//    boolean existsOverlap(String name);
//
//    Optional<SchedulesModel> findByNameIgnoreCase(String name);
}
