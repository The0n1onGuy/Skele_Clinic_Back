package com.nexusbusiness.repositories;

import com.nexusbusiness.models.shoppingcart.InventoryModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface IPosInventoryRepository extends JpaRepository<InventoryModel, Long> {
    boolean existsByNameIgnoreCase(String name);

    Optional<InventoryModel> findByNameIgnoreCase(String name);
}
