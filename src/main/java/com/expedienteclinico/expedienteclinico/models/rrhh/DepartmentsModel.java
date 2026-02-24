package com.expedienteclinico.expedienteclinico.models.rrhh;
import com.expedienteclinico.expedienteclinico.models.StatusModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import javax.persistence.Table;
import java.util.UUID;

@Entity
//@Table(appliesTo = "Departments")
//@Table
@Table(name = "rrhh_departments")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DepartmentsModel {

    @Column(updatable = false, nullable = false, columnDefinition = "UNIQUEIDENTIFIER")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID uuid;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_department", nullable = false)
    private Long id;


    @Column(length = 100, nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_status", nullable = false) //
    private StatusModel status;
}
