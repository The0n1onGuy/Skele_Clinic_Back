package com.nexusbusiness.models.shoppingcart;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Entity(name = "pos_bundles")
@Table(name = "pos_bundles")
@Getter @Setter @NoArgsConstructor
public class BundleModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "reference_sku", unique = true)
    private String reference_sku;

    @Lob
    private String description;

    @Column(name = "is_seasonal_offer")
    private Boolean is_seasonal_offer = false;

    @OneToMany(mappedBy = "bundle_id_fk", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BundleLineModel> lines;

    @Column(name = "total_normal_value", precision = 12, scale = 2)
    private BigDecimal total_normal_value;

    @Column(name = "bundle_price", precision = 12, scale = 2)
    private BigDecimal bundle_price;
}