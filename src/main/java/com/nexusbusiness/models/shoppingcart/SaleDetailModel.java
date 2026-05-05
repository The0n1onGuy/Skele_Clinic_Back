package com.nexusbusiness.models.shoppingcart;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pos_sale_details")
@Getter
@Setter
@NoArgsConstructor
public class SaleDetailModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sale_id", nullable = false)
    private SaleModel sale_id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductModel product_id;

    @Column(name = "sold_price", precision = 10, scale = 2, nullable = false)
    private java.math.BigDecimal sold_price; // The price at the EXACT moment of sale

    @Column(nullable = false)
    private Integer quantity;

    @Column(precision = 12, scale = 2, nullable = false)
    private java.math.BigDecimal subtotal; // (sold_price * quantity)
}