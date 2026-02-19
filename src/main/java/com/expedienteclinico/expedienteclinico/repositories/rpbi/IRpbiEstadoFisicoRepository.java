package com.expedienteclinico.expedienteclinico.repositories.rpbi;

import com.expedienteclinico.expedienteclinico.models.rpbi.RpbiEstadoFisicoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRpbiEstadoFisicoRepository extends JpaRepository<RpbiEstadoFisicoModel, Long>{
}
