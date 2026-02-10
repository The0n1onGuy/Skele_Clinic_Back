package com.expedienteclinico.expedienteclinico.models;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
@Entity
@Table(name = "estatus")
@Getter
@Setter
public class StatusModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Recuerda: IDENTITY es mejor para SQL Server/MySQL
    @Column(nullable = false)
    private Long id;

    private String statusName;
}