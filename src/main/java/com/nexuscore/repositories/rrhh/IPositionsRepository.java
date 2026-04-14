package com.nexuscore.repositories.rrhh;
import com.nexuscore.models.rrhh.DepartmentsModel;
import com.nexuscore.models.rrhh.PositionsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IPositionsRepository extends JpaRepository<PositionsModel, Long> {
    boolean existsByNameIgnoreCase(String name);

    Optional<DepartmentsModel> findByNameIgnoreCase(String name);
}
