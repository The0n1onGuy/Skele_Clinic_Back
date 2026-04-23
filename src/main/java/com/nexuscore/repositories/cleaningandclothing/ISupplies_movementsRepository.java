package com.nexuscore.repositories.cleaningandclothing;

import com.nexuscore.models.cleaningandclothing.Supplies_movementsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISupplies_movementsRepository extends JpaRepository<Supplies_movementsModel, Long> {

    // Buscar por tipo de movimiento
    List<Supplies_movementsModel> findByMovementType(String movementType);

    // Buscar por insumo
    List<Supplies_movementsModel> findBySupply(Long suppliesId);

}