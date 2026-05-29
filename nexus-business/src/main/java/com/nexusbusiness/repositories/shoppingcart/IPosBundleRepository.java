package com.nexusbusiness.repositories.shoppingcart;
import com.nexusbusiness.models.shoppingcart.BundleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface IPosBundleRepository extends JpaRepository<BundleModel, Long> {

}
