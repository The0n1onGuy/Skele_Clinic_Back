package com.expedienteclinico.expedienteclinico.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.Table;


import javax.persistence.*;



@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Articulos_textilesModel {

    @Id
    @GeneratedValue( strategy = GenerationType.SEQUENCE )
    @Column( nullable = false )

    private Long id_articulo;
    private String nombre;
    private String descripcion;
}


