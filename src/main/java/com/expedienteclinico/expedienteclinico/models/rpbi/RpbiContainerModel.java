package com.expedienteclinico.expedienteclinico.models.rpbi;

import com.expedienteclinico.expedienteclinico.models.StatusModel;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Table(name = "rpbi_cat_envase")
@Getter
@Setter
public class RpbiContainerModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre; // "Bolsa de Polietileno", "Recipiente Rígido"

    @Column(length = 255)
    private String descripcion; // "Impermeable, calibre 200..."

    @ManyToOne
    @JoinColumn(name = "estatus_id", nullable = false)
    private StatusModel estatus;
}