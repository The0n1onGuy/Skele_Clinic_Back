package com.expedienteclinico.expedienteclinico.models.rrhh;
import com.expedienteclinico.expedienteclinico.models.system.StatusModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "rrhh_departments")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DepartmentsModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_department", nullable = false)
    private Long id;

//    @Column(updatable = false, nullable = false, columnDefinition = "UNIQUEIDENTIFIER")
    @Column(updatable = false, nullable = false)
    private UUID uuid = UUID.randomUUID();

    @Column(length = 100, nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "id_status", nullable = false) //
    private StatusModel id_status;
}
