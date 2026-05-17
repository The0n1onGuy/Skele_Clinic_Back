package com.nexushiscore.models.cleaningandclothing;

import com.nexushiscore.models.system.StatusModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "cat_cleaning_supplies")
@Getter
@Setter
public class Cleaning_suppliesModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long supplies_id;

    @Column(name = "uuid", updatable = false, nullable = false, unique = true, length = 36)
    private String uuid = UUID.randomUUID().toString();

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "unit_measurement", length = 50)
    private String unitMeasurement;

    @Column(name = "current_stock")
    private Integer currentStock;

    @Column(name = "stock_min")
    private Integer stockMin;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private StatusModel status;
}