package com.nexusbusiness.repositories.shoppingcart;

import com.nexusbusiness.models.shoppingcart.SaleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface IPosSaleRepository extends JpaRepository<SaleModel, Long> {

}
