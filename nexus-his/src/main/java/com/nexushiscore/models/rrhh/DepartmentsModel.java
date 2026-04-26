package com.nexushiscore.models.rrhh;

import com.nexushiscore.models.system.StatusModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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

    /*@Column(updatable = false, nullable = false, columnDefinition = "UNIQUEIDENTIFIER")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID uuid;*/
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(updatable = false, nullable = false, length = 36)
    private UUID uuid = UUID.randomUUID();



    @Column(length = 100, nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "id_status", nullable = false) //
    private StatusModel id_status;
}
