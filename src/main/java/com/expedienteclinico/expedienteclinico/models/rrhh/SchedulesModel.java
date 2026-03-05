package com.expedienteclinico.expedienteclinico.models.rrhh;

import com.expedienteclinico.expedienteclinico.models.system.StatusModel;
import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "rrhh_schedules")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SchedulesModel {

    @Column(updatable = false, nullable = false, columnDefinition = "UNIQUEIDENTIFIER")
    private UUID uuid = UUID.randomUUID();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_schedule", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_employee", nullable = false)
    private EmployeesModel employee;

    @Column(name = "day_week", nullable = false, length = 15)
    private String dayOfWeek;

    @Column(name = "start_time", nullable = false)
    private String startTime;

    @Column(name = "end_time", nullable = false)
    private String endTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_status", nullable = false) //
    private StatusModel status;
}