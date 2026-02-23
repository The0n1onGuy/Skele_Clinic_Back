package com.expedienteclinico.expedienteclinico.models;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Table (name = "status")
@Getter

public class StatusModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    private Long id;
    @Column(name = "status_name",nullable = false)
    private String statusName ;

}