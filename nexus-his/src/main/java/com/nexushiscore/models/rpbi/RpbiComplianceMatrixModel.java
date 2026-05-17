package com.nexushiscore.models.rpbi;

import com.nexushiscore.models.system.StatusModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "rpbi_compliance_matrix_nom087")
@Getter
@Setter
public class RpbiComplianceMatrixModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relaciones estrictas (LAZY para optimizar el grafo de memoria)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classification_id", nullable = false)
    private RpbiClasificationModel classification;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "physical_state_id", nullable = false)
    private RpbiPhysicalStateModel physicalState;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "container_id", nullable = false)
    private RpbiContainerModel container;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", nullable = false)
    private StatusModel status;
}