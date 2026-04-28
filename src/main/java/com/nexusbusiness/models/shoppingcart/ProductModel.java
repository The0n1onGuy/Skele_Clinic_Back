package com.nexusbusiness.models.shoppingcart;
import com.nexusbusiness.models.PosStatusModel;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.util.UUID;

public class ProductModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(updatable = false, nullable = false, length = 36, unique = true)
    private UUID uuid = UUID.randomUUID();

    @Column(nullable = false, unique = true, length = 50)
    private String sku;

    @Column(nullable = false, length = 150)
    private String name;

    // precision 10, scale 2 = 12345678.99
    @Column(name = "base_price", precision = 10, scale = 2, nullable = false)
    private java.math.BigDecimal basePrice;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryModel category;

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private PosStatusModel status;
}
