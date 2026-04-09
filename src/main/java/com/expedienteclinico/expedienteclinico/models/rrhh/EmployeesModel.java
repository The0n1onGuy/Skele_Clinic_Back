package com.expedienteclinico.expedienteclinico.models.rrhh;
import com.expedienteclinico.expedienteclinico.models.system.StatusModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(name = "rrhh_employees")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmployeesModel {

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(updatable = false, nullable = false, length = 36)
    private UUID uuid = UUID.randomUUID();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_employee", nullable = false)
    private Long id;

    @Column(updatable = false, nullable = false, columnDefinition = "UNIQUEIDENTIFIER")
    private UUID uuid = UUID.randomUUID();

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String patname;

    @Column(nullable = false)
    private String matname;

    @Column(nullable = false)
    private String curp;

    @Column(nullable = false)
    private String rfc;

    // CORRECCIÓN: Tipado estricto de fechas
    @Column(name = "datebirth", nullable = false)
    private java.time.LocalDate datebirth;

    @Column(name = "datereg", nullable = false)
    private java.time.LocalDate datereg;

    @Column(nullable = false)
    private String gender;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_position", nullable = false)
    private PositionsModel id_position;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_department" , nullable = false)
    private DepartmentsModel id_department;

    @ManyToOne
    @JoinColumn(name = "id_status", nullable = false)
    private StatusModel id_status;

}



