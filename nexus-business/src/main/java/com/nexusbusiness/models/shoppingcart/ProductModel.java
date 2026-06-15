package com.nexusbusiness.models.shoppingcart;
import com.nexusbusiness.models.system.StatusModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.util.UUID;
@Entity
@Table(name = "pos_products")
@Getter
@Setter
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

    @Column(nullable = true, length = 150)
    private String base_description;

    // precision 10, scale 2 = 12345678.99
    @Column( precision = 10, scale = 2, nullable = false)
    private java.math.BigDecimal base_price;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private PosCategoryModel category_id;

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private StatusModel status_id;
}
