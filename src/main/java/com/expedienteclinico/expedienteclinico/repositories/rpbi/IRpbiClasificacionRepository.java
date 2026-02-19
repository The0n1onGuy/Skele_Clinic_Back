package com.expedienteclinico.expedienteclinico.repositories.rpbi;

import com.expedienteclinico.expedienteclinico.models.rpbi.RpbiClasificacionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRpbiClasificacionRepository extends JpaRepository<RpbiClasificacionModel, Long>{
}
