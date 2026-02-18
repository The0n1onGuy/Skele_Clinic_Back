package com.expedienteclinico.expedienteclinico.models.rrhh;
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
@Table(name = "department")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DepartmentsModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Long id;

    /*
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "uuid", updatable = false, nullable = false)
    */
    @Column(name = "uuid", updatable = false, nullable = false, columnDefinition = "UNIQUEIDENTIFIER")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID uuid;

    @Column(name = "dep_name", length = 100)
    private String dep_name;
}
