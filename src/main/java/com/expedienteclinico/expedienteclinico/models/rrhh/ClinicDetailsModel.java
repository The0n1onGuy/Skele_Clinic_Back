package com.expedienteclinico.expedienteclinico.models.rrhh;

import com.expedienteclinico.expedienteclinico.models.StatusModel;
import lombok.*;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "rrhh_clinic_details")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClinicDetailsModel {

    @Column(updatable = false, nullable = false, columnDefinition = "UNIQUEIDENTIFIER")
    private UUID uuid = UUID.randomUUID();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_clinic_details", nullable = false)
    private Long id;

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
    private StatusModel status;

}