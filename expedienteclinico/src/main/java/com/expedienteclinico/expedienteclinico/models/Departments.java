package com.expedienteclinico.expedienteclinico.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;

@Entity
//@Table(appliesTo = "Departments")
@Table(name = "Departments")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Departments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long UUID;
    private Long id;
    private String dep_name;
}
