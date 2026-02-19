package com.expedienteclinico.expedienteclinico.repositories.rpbi;

import com.expedienteclinico.expedienteclinico.models.rpbi.RpbiEnvaseModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRpbiEnvaseRepository extends JpaRepository<RpbiEnvaseModel, Long>{
}
