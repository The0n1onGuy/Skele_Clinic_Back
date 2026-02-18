package com.expedienteclinico.expedienteclinico.repositories.rrhh;
import com.expedienteclinico.expedienteclinico.models.rrhh.PositionsModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPositionsRepo extends JpaRepository<PositionsModel, Integer> {
}
