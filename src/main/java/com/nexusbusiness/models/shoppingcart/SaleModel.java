package com.nexusbusiness.models.shoppingcart;
import com.nexusbusiness.models.PosStatusModel;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.util.UUID;

public class SaleModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(updatable = false, nullable = false, length = 36)
    private UUID uuid = UUID.randomUUID();

    @Column(name = "ticket_number", unique = true, nullable = false, length = 20)
    private String ticketNumber;

    @Column(name = "total_amount", precision = 12, scale = 2, nullable = false)
    private java.math.BigDecimal totalAmount;

    @Column(name = "sale_date", nullable = false)
    private java.time.LocalDateTime saleDate = java.time.LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private PosStatusModel status;
}
