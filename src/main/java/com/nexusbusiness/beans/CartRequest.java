package com.nexusbusiness.beans;
import java.util.List;
import lombok.Data;

@Data
public class CartRequest {
    private List<CartItem> items;
    private String paymentMethod;
}

