package com.nexusbusiness.repositories.shoppingcart;

import com.nexusbusiness.models.InventoryMovementModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface IPosMovementRepository extends JpaRepository<InventoryMovementModel, Long> {

}
