package com.expedienteclinico.expedienteclinico.models;


import jakarta.persistence.Table;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table( name = "first_model" )
@Deprecated
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class FirstModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    private Long id;
    private String nombre;
    private String apellido;

}
