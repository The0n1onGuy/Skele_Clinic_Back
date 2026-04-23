package com.nexuscore.models.rpbi;

import com.nexuscore.models.system.StatusModel;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

@Entity
@Table(name = "rpbi_cat_phyisical_state")
@Getter
@Setter
public class RpbiPhysicalStateModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid", updatable = false, nullable = false, unique = true, length = 36)
    private String uuid = java.util.UUID.randomUUID().toString();

    @Column(nullable = false, length = 50, unique = true)
    private String name; // "Sólido", "Líquido"

    @Column(name = "measure_unit", nullable = false, length = 10)
    private String measureUnit; // "kg", "L" (Simplificamos aquí para no crear otra tabla)

    // Relación obligatoria con Estatus
    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private StatusModel status;
}