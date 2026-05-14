package com.nexusbusiness.beans;
import java.util.List;
import lombok.Data;

@Data
public class CartRequestObject {
    private List<CartItemObject> items;
    private String paymentMethod;
}

