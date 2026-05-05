package com.nexusbusiness.repositories;

import com.nexusbusiness.models.shoppingcart.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface IPosProductRepository extends JpaRepository<ProductModel, Long> {
    boolean existsByNameIgnoreCase(String name);

    Optional<ProductModel> findByNameIgnoreCase(String name);
}
