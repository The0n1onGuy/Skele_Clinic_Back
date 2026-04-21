package com.nexuscore.repositories.system;

import com.nexuscore.models.system.SystemRolesModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ISystemRolesRepository extends JpaRepository<SystemRolesModel, Long> {
    // Metodo esencial para que Spring Security busque roles por su nombre
    Optional<SystemRolesModel> findByRoleName(String roleName);
}
