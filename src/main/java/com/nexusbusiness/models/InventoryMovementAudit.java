package com.nexusbusiness.models;
import com.nexusbusiness.models.PosStatusModel;
import com.nexusbusiness.models.shoppingcart.ProductModel;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.util.UUID;

public class InventoryMovementAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductModel product;

    @Column(nullable = false, length = 20)
    private String type; // e.g., SALE, RESTOCK

    @Column(nullable = false)
    private Integer quantity;

    @Lob // MySQL will treat this as a TEXT field for audit notes
    private String reason;

    @Column(name = "movement_date", nullable = false)
    private java.time.LocalDateTime movementDate = java.time.LocalDateTime.now();
}
