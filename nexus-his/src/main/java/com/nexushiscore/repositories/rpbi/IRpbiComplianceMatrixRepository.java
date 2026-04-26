package com.nexushiscore.repositories.rpbi;

import com.nexushiscore.models.rpbi.RpbiClasificationModel;
import com.nexushiscore.models.rpbi.RpbiComplianceMatrixModel;
import com.nexushiscore.models.rpbi.RpbiContainerModel;
import com.nexushiscore.models.rpbi.RpbiPhysicalStateModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRpbiComplianceMatrixRepository extends JpaRepository<RpbiComplianceMatrixModel, Long> {

    // Spring Data JPA construye el query automáticamente evaluando las 3 entidades y el estatus.
    boolean existsByClassificationAndPhysicalStateAndContainerAndStatus_Id(
            RpbiClasificationModel classification,
            RpbiPhysicalStateModel physicalState,
            RpbiContainerModel container,
            Long statusId
    );
}