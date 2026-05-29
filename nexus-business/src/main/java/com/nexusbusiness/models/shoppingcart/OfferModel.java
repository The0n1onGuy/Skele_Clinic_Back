package com.nexusbusiness.models.shoppingcart;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "pos_offers")
@Table(name = "pos_offers")
@Getter @Setter @NoArgsConstructor
public class OfferModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "discount_type", nullable = false, length = 20)
    private String discount_type; // "PERCENTAGE" or "FIXED"

    @Column(name = "discount_value", precision = 10, scale = 2, nullable = false)
    private BigDecimal discount_value;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(name = "start_date")
    private LocalDateTime start_date;

    @Column(name = "end_date")
    private LocalDateTime end_date;

    //BRIDGE TABLE
    @ManyToMany
    @JoinTable(
            name = "pos_offer_categories",
            //CALL THE CURRENT ID REFERENCE
            joinColumns = @JoinColumn(name = "offer_id"),
            //THEN TO THE FIELD TO REFER TO
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<PosCategoryModel> categories;
    // SAME LOGIC BUT REFER TO PRODUCT AND LOOK FOR OFFER
    @ManyToMany
    @JoinTable(
            name = "pos_offer_products",
            joinColumns = @JoinColumn(name = "offer_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<ProductModel> products;
}