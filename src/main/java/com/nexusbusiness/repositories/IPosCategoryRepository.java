package com.nexusbusiness.repositories;

import com.nexusbusiness.models.shoppingcart.InventoryModel;
import com.nexusbusiness.models.shoppingcart.PosCategoryModel;
import com.nexuscore.models.system.StatusModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IPosCategoryRepository extends JpaRepository<PosCategoryModel, Long> {
    Optional<PosCategoryModel> findByNameIgnoreCase(String name);
}
