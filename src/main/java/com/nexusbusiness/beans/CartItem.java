package com.nexusbusiness.beans;
import lombok.Data;

@Data
public class CartItem {
    private Long productId;
    private Integer quantity;
}
