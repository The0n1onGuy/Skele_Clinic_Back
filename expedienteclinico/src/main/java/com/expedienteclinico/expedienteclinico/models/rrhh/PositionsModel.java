
package com.expedienteclinico.expedienteclinico.models.rrhh;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import javax.persistence.Table;
import javax.xml.soap.Text;
import java.util.UUID;

@Entity
@Table(name = "positions")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PositionsModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long pos_id;

    @Column(name = "uuid", updatable = false, nullable = false, columnDefinition = "UNIQUEIDENTIFIER")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID pos_uuid;

    @Column(name = "position_name", length = 100, nullable = false)
    private String pos_name;
    @Column(name = "position_description")
    private Text pos_description;
}
