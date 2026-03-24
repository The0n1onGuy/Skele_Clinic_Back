package com.expedienteclinico.expedienteclinico.models.rrhh;
import com.expedienteclinico.expedienteclinico.models.system.StatusModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;

@Entity
@Table(name = "rrhh_employees")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmployeesModel {

    @Column(updatable = false, nullable = false, columnDefinition = "UNIQUEIDENTIFIER")
    private UUID uuid = UUID.randomUUID();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_employee", nullable = false)
    private Long id;

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

    @Column(nullable = false)
    private String datebirth;

    @Column(nullable = false)
    private String datereg;

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



