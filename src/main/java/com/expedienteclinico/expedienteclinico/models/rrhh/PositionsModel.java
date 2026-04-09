
package com.expedienteclinico.expedienteclinico.models.rrhh;
import com.expedienteclinico.expedienteclinico.models.system.StatusModel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(name = "rrhh_positions")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PositionsModel {

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(updatable = false, nullable = false, length = 36)
    private UUID uuid = UUID.randomUUID();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_position", nullable = false)
    private Long id;

    @Column(updatable = false, nullable = false, columnDefinition = "UNIQUEIDENTIFIER")
    private UUID uuid = UUID.randomUUID();

    @Column(length = 100, nullable = false)
    private String name;
    @Column(columnDefinition = "TEXT", nullable = true)
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_status", nullable = false) //
    private StatusModel id_status;
}
