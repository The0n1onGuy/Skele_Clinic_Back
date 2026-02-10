package com.expedienteclinico.expedienteclinico.models;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Table
@Getter

public class StatusModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    private Long id;

    private String statusName ;

}
