package com.nexusbusiness.beans;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CartCalculationResponseObject {
    private BigDecimal subtotal;
    private BigDecimal totalDiscount;
    private BigDecimal finalTotal;
    private List<CalculatedItem> details;

    // Getters and Setters
    @Data
    public static class CalculatedItem {
        private String sku;
        private String name;
        private int quantity;
        private BigDecimal originalUnitPrice;
        private BigDecimal finalUnitPrice; // Price after offer
        private String appliedOfferName; // "Abarrotes 10% Off"
    }
}

