package com.nexusbusiness.beans;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class SaleResponseObject {
    private UUID uuid;
    private String ticketNumber;
    private BigDecimal totalAmount;
    private BigDecimal taxAmount;
    private LocalDateTime saleDate;
    private String paymentMethod;
    private String statusName;

    private List<SaleDetailObject> details;
}