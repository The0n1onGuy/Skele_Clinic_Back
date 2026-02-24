package com.expedienteclinico.expedienteclinico.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*; // USAMOS JAVAX PARA SPRING BOOT 2.6
import javax.persistence.Table;

@Entity
@Table(name = "estatus")
@NoArgsConstructor
@AllArgsConstructor
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