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
public class Movimientos_insumosModel {

    @Id
    @GeneratedValue( strategy = GenerationType.SEQUENCE )
    @Column( nullable = false )

    private Long id_movimiento;

    // private Integer id_insumo;

    private String tipo_movimiento; //(Entrada, Consumo, ajuste)

    private Integer cantidad;

    private String fecha;

   // private Integer id_empleado;

    private String observaciones;
}
