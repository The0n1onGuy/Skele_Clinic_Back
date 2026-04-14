package com.nexuscore.models.rpbi;

import com.nexuscore.models.system.StatusModel;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

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