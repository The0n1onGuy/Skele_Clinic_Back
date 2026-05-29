package com.nexusbusiness.repositories.shoppingcart;

import com.nexusbusiness.models.shoppingcart.PosCategoryModel;
import com.nexusbusiness.models.system.StatusModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IPosCategoryRepository extends JpaRepository<PosCategoryModel, Long> {
    Optional<PosCategoryModel> findByNameIgnoreCase(String name);
}
