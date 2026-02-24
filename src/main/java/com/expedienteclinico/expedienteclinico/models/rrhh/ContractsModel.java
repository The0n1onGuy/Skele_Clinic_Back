package com.expedienteclinico.expedienteclinico.models.rrhh;

import com.expedienteclinico.expedienteclinico.models.StatusModel;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "rrhh_contracts")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ContractsModel {

    @Column(updatable = false, nullable = false, columnDefinition = "UNIQUEIDENTIFIER")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID uuid;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_contract", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_employee", nullable = false)
    private EmployeesModel employee;

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
    private StatusModel status;
}