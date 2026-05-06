package com.nexusbusiness.repositories;

import com.nexusbusiness.models.shoppingcart.InventoryModel;
import com.nexusbusiness.models.shoppingcart.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface IPosInventoryRepository extends JpaRepository<InventoryModel, Long> {

    Optional<InventoryModel> findByProduct(ProductModel product_id);
}
