package com.nexusbusiness.models.shoppingcart;
import com.nexusbusiness.models.system.StatusModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.util.UUID;
@Entity
@Table(name = "pos_sales")
@Getter
@Setter
public class SaleModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(updatable = false, nullable = false, length = 36)
    private UUID uuid = UUID.randomUUID();

    @Column(name = "ticket_number", unique = true, nullable = false, length = 20)
    private String ticket_number;

    @Column(name = "total_amount", precision = 12, scale = 2, nullable = false)
    private java.math.BigDecimal total_amount;

    @Column(name = "payment_method", length = 50) // Matches snake_case in MySQL
    private String payment_method;

    @Column(name = "sale_date", nullable = false)
    private java.time.LocalDateTime sale_date = java.time.LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private StatusModel status_id;
}
