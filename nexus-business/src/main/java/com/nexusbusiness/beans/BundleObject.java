package com.nexusbusiness.beans;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class BundleObject {
    private Long id;
    private String name;
    private String reference;
    private String description;
    private Boolean isSeasonalOffer;
    private BigDecimal bundlePrice; // Optional: If empty, the service calculates the 10% discount

    // The list of items inside the bundle
    private List<BundleLineObject> lines;
}