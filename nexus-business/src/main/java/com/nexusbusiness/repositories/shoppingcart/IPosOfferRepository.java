package com.nexusbusiness.repositories.shoppingcart;
import com.nexusbusiness.models.shoppingcart.OfferModel;
import com.nexusbusiness.models.shoppingcart.PosCategoryModel;
import com.nexusbusiness.models.shoppingcart.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface IPosOfferRepository extends JpaRepository<OfferModel, Long> {
    @Query("SELECT DISTINCT o FROM pos_offers o " +
            "LEFT JOIN o.products p " +
            "LEFT JOIN o.categories c " +
            "WHERE o.active = true " +
            "AND (p = :product OR c = :category)")
    List<OfferModel> findActiveOffersForProduct(@Param("product") ProductModel product,
                                                @Param("category") PosCategoryModel category);
}
