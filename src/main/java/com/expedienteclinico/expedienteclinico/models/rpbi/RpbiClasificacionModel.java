package com.expedienteclinico.expedienteclinico.models.rpbi;

import com.expedienteclinico.expedienteclinico.models.StatusModel;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Table(name = "rpbi_cat_clasificacion")
@Getter
@Setter
public class RpbiClasificacionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(length = 255)
    private String descripcion;

    @Column(name = "codigo_color", length = 20)
    private String codigoColor;

    // Columna equivalente de FK a StatusModel instanciandolo abajo
    @ManyToOne
    @JoinColumn(name = "estatus_id", nullable = false)
    private StatusModel estatus;
    //          ↑ Aquí
}