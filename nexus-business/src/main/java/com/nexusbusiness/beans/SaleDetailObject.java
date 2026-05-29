package com.nexusbusiness.beans;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class SaleDetailObject {
    private String productName; // Better for receipts than just the ID
    private String productSku;
    private Integer quantity;
    private BigDecimal soldPrice;
    private BigDecimal subtotal;
}