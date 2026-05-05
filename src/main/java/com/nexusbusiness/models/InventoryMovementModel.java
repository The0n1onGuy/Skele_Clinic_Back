package com.nexusbusiness.models;
import com.nexusbusiness.models.shoppingcart.ProductModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "pos_sale")
@Getter
@Setter
public class InventoryMovementModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductModel product_id;

    @Column(nullable = false, length = 20)
    private String type; // e.g., SALE, RESTOCK

    @Column(nullable = false)
    private Integer quantity;

    @Lob // MySQL will treat this as a TEXT field for audit notes
    private String reason;

    @Column(name = "movement_date", nullable = false)
    private java.time.LocalDateTime movement_date = java.time.LocalDateTime.now();
}
