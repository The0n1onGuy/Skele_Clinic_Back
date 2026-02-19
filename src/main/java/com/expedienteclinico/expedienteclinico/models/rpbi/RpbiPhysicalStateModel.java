package com.expedienteclinico.expedienteclinico.models.rpbi;

import com.expedienteclinico.expedienteclinico.models.StatusModel;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Table(name = "rpbi_cat_estado_fisico")
@Getter
@Setter
public class RpbiPhysicalStateModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50, unique = true)
    private String nombre; // "Sólido", "Líquido"

    @Column(nullable = false, length = 10)
    private String unidadMedida; // "kg", "L" (Simplificamos aquí para no crear otra tabla)

    // Relación obligatoria con Estatus
    @ManyToOne
    @JoinColumn(name = "estatus_id", nullable = false)
    private StatusModel estatus;
}