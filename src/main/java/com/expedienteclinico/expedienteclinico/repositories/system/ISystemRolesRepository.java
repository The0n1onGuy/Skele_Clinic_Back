package com.expedienteclinico.expedienteclinico.repositories.system;

import com.expedienteclinico.expedienteclinico.models.system.SystemRolesModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISystemRolesRepository extends JpaRepository<SystemRolesModel, Long> {
}
