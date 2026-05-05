package com.nexusbusiness.repositories;

import com.nexusbusiness.models.InventoryMovementModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface IPosMovementRepository extends JpaRepository<InventoryMovementModel, Long> {
    boolean existsByNameIgnoreCase(String name);

    Optional<InventoryMovementModel> findByNameIgnoreCase(String name);
}
