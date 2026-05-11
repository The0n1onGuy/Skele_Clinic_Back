package com.nexusbusiness.repositories;
import com.nexusbusiness.models.shoppingcart.OfferModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface IPosOfferRepository extends JpaRepository<OfferModel, Long> {

}
