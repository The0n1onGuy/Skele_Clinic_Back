package com.nexusbusiness.models.shoppingcart;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "pos_bundle_lines")
@Table(name = "pos_bundle_lines")
@Getter @Setter @NoArgsConstructor
public class BundleLineModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bundle_id_fk", nullable = false)
    private BundleModel bundle_id_fk;

    @ManyToOne
    @JoinColumn(name = "product_id_fk", nullable = false)
    private ProductModel product_id_fk;

    @Column(nullable = false)
    private Integer quantity = 1;
}