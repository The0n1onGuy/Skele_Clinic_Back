package com.nexusbusiness.beans;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ProductCatalogResponseObject {
    private UUID uuid;
    private String product_sku;
    private String product_name;
    private String product_description;
    private BigDecimal normal_price;
    private String category_name;
    private String status_name;
    private BigDecimal discount_value;
    private String discount_type;
    private BigDecimal discountPrice;
}