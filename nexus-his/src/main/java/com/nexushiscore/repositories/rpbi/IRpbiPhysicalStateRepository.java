package com.nexushiscore.repositories.rpbi;

import com.nexushiscore.models.rpbi.RpbiPhysicalStateModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRpbiPhysicalStateRepository extends JpaRepository<RpbiPhysicalStateModel, Long>{

    // Para poder "Traducir" de UUID a ID en las operaciones internas
    Optional<RpbiPhysicalStateModel> findByUuid(String uuid);

}
