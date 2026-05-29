package com.nexusbusiness.beans;
import java.util.List;
import lombok.Data;

@Data
public class CartRequestObject {
    //private List<CartItemObject> object_items;
    private List<CartItemRequest> items;
    private String paymentMethod;

    @Data
    public static class CartItemRequest {
        private String sku;
        private int quantity;
        // Getters and Setters
    }
}

