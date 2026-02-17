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
public class Empleados_LyRModel {

    @Id
    @GeneratedValue( strategy = GenerationType.SEQUENCE )
    @Column( nullable = false )

    private Long id_empleado ;
    private String nombre;
    private String apellido;
    private String area;
    private String turno;
    private Integer telefono;
    private String estatus;
}


