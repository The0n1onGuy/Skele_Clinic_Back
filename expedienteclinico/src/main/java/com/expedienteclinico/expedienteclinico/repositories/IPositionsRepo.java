package com.expedienteclinico.expedienteclinico.repositories;
import com.expedienteclinico.expedienteclinico.models.Positions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPositionsRepo extends JpaRepository<Positions, Integer> {
}
