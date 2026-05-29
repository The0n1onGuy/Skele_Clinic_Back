package com.nexusbusiness.services;

import com.nexusbusiness.models.shoppingcart.BundleLineModel;
import com.nexusbusiness.models.shoppingcart.BundleModel;
import com.nexusbusiness.repositories.shoppingcart.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class PosBundleService {

    private final IPosBundleRepository bundleRepository;

    public PosBundleService(IPosBundleRepository bundleRepository) {
        this.bundleRepository = bundleRepository;
    }

    @Transactional
    public BundleModel saveBundle(BundleModel bundle) {

        // Computation Logic
        if (bundle.getLines() != null && !bundle.getLines().isEmpty()) {
            BigDecimal normalTotal = BigDecimal.ZERO;

            for (BundleLineModel line : bundle.getLines()) {
                if (line.getProduct_id_fk() != null && line.getProduct_id_fk().getBase_price() != null) {
                    BigDecimal lineTotal = line.getProduct_id_fk().getBase_price()
                            .multiply(new BigDecimal(line.getQuantity()));
                    normalTotal = normalTotal.add(lineTotal);

                    // Ensure bi-directional relationship is set before saving
                    line.setBundle_id_fk(bundle);
                }
            }
            bundle.setTotal_normal_value(normalTotal);

            // Apply default 10% discount if bundlePrice is not manually provided
            if (bundle.getBundle_price() == null || bundle.getBundle_price().compareTo(BigDecimal.ZERO) == 0) {
                bundle.setBundle_price(normalTotal.multiply(new BigDecimal("0.90")));
            }
        } else {
            // Failsafe for empty bundles
            bundle.setTotal_normal_value(BigDecimal.ZERO);
            bundle.setBundle_price(BigDecimal.ZERO);
        }

        return bundleRepository.save(bundle);
    }
}