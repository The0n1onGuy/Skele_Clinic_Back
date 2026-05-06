package com.nexusbusiness.repositories;

import com.nexusbusiness.models.shoppingcart.SaleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface IPosSaleRepository extends JpaRepository<SaleModel, Long> {

}
