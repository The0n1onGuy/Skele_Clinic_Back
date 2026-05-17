package com.nexushiscore.models.emergencies;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table( name = "doctor_model" )
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class DoctorModel {
    @Id
    @GeneratedValue( strategy = GenerationType.SEQUENCE )
    @Column( nullable = false )
    private Long id ;
    private String nombre ;
    private String apellido ;

}
