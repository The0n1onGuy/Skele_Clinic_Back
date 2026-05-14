package com.nexusbusiness.beans;

import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ProductObject {
    private UUID uuid;
    private String sku;
    private String name;
    private BigDecimal basePrice;
    private String categoryName;
    private String statusName;
}