package com.expedienteclinico.expedienteclinico.models.rrhh;

import com.expedienteclinico.expedienteclinico.models.system.StatusModel;
import lombok.*;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "rrhh_contracts")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ContractsModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_contract", nullable = false)
    private Long id;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(updatable = false, nullable = false, length = 36)
    private UUID uuid = UUID.randomUUID();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_employee", nullable = false)
    private EmployeesModel id_employee;

    @Column(name = "contract_type", nullable = false, length = 50)
    private String contractType;

    @Column(name = "hiring_date", nullable = false)
    private String hiringDate;

    @Column(name = "termination_date")
    private String terminationDate;

    @Column(name = "base_salary", nullable = false, precision = 18, scale = 2)
    private BigDecimal baseSalary;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_status", nullable = false) //
    private StatusModel id_status;
}