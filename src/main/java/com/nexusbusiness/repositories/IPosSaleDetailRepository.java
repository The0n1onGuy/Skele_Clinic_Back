package com.nexusbusiness.repositories;

import com.nexusbusiness.models.shoppingcart.SaleDetailModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface IPosSaleDetailRepository extends JpaRepository<SaleDetailModel, Long> {

}
