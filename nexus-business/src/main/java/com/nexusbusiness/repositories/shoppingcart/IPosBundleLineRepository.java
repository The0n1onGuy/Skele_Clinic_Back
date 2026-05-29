package com.nexusbusiness.repositories.shoppingcart;
import com.nexusbusiness.models.shoppingcart.BundleLineModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface IPosBundleLineRepository extends JpaRepository<BundleLineModel, Long> {

}
