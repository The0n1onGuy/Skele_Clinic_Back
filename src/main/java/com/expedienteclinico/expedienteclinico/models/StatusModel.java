package com.expedienteclinico.expedienteclinico.models;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*; // USAMOS JAVAX PARA SPRING BOOT 2.6

@Entity
@Table(name = "estatus")
@Getter
@Setter
public class StatusModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estatus", nullable = false) // Buena práctica: nombrar explícitamente la PK
    private Long id;

    @Column(name = "nombre", nullable = false, unique = true)
    private String statusName;
}