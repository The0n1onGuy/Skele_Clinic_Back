package com.nexusbusiness.services;

import com.nexusbusiness.models.shoppingcart.OfferModel;
import com.nexusbusiness.repositories.shoppingcart.IPosOfferRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class PosOfferService {

    private final IPosOfferRepository offerRepository;

    public PosOfferService(IPosOfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    @Transactional
    public OfferModel saveOffer(OfferModel offer) {
        // Validation Logic
        if ("PERCENTAGE".equalsIgnoreCase(offer.getDiscount_type())) {
            if (offer.getDiscount_value().compareTo(BigDecimal.ZERO) <= 0 ||
                    offer.getDiscount_value().compareTo(new BigDecimal("100")) > 0) {
                throw new IllegalArgumentException("Discount value must be between 1-100.");
            }
        }

        return offerRepository.save(offer);
    }
}