package com.nexusbusiness.beans;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OfferObject {
    private Long id;
    private String name;
    private String discountType; // "PERCENTAGE" or "FIXED"
    private BigDecimal discountValue;
    private Boolean active;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    // Arrays of IDs sent from the frontend
    private List<Long> categoryIds;
    private List<Long> productIds;
}