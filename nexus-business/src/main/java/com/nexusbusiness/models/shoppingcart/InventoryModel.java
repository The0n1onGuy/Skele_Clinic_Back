package com.nexusbusiness.models.shoppingcart;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pos_inventory")
@Getter
@Setter
@NoArgsConstructor
public class InventoryModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "product_id", nullable = false, unique = true)
    private ProductModel product;

    @Column(name = "current_stock", nullable = false)
    private Integer current_stock = 0;

    @Column(name = "min_stock", nullable = false)
    private Integer min_stock = 5; // Alert threshold for low stock

    @Column(name = "last_restock_date")
    private java.time.LocalDateTime last_restock_date;
}
