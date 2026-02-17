package com.expedienteclinico.expedienteclinico.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Insumo_limpiezaModel {

    @Id
    @GeneratedValue( strategy = GenerationType.SEQUENCE )
    @Column( nullable = false )

    private Long id_insumo;

    private String nombre; // (Cloro, Desinfectante, Alcohol)

    private String unidad_medida; //(Kg, L, Ml,)

    private Integer stock_actual;

    private Integer stock_minimo;

    private String fecha_caducidad;

    private String estado;
}


