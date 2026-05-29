package com.nexusbusiness.repositories.shoppingcart;

import com.nexusbusiness.models.shoppingcart.SaleDetailModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface IPosSaleDetailRepository extends JpaRepository<SaleDetailModel, Long> {

}
