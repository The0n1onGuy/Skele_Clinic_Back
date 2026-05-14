package com.nexusbusiness.beans;
import lombok.Data;
import java.util.UUID;


@Data
public class CartItemObject {
    private UUID productUUID;
    private Integer quantity;
}
