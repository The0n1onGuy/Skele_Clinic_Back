package com.expedienteclinico.expedienteclinico.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Table(name = "morgue")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Morgue {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    private Long id;

    private Long id_paciente;
    private String causa_defuncion;
    private String fecha_ingreso;
    private String estado;
    private Integer numero_gaveta;
}