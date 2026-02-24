package com.expedienteclinico.expedienteclinico.repositories;
import com.expedienteclinico.expedienteclinico.models.StatusModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IStatusRepository extends JpaRepository<StatusModel, Long > {
}
