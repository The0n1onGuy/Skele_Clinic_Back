package com.expedienteclinico.expedienteclinico.models.rrhh;

import com.expedienteclinico.expedienteclinico.models.system.StatusModel;
import lombok.*;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(name = "rrhh_clinic_details")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClinicDetailsModel {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_clinic_details", nullable = false)
    private Long id;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(updatable = false, nullable = false, length = 36)
    private UUID uuid = UUID.randomUUID();

    @OneToOne
    @JoinColumn(name = "id_employee", unique = true, nullable = false)
    private EmployeesModel employee;

    @Column(name = "professional_license", unique = true, length = 20)
    private String professionalLicense;

    @Column(name = "graduation_institution", length = 150)
    private String graduationInstitution;

    @Column(length = 100)
    private String specialty;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_status", nullable = false) //
    private StatusModel id_status;

}