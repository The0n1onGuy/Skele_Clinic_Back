package com.nexushiscore.repositories.rrhh;
import com.nexushiscore.models.rrhh.SchedulesModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISchedulesRepository extends JpaRepository<SchedulesModel, Long> {
//    boolean existsOverlap(String name);
//
//    Optional<SchedulesModel> findByNameIgnoreCase(String name);
}
