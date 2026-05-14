package com.nexusbusiness.beans;

import lombok.Data;

@Data
public class InventoryObject {
    private Long productId;
    private Integer currentStock;
    private Integer minStock;
}