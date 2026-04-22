package com.nexuscore.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table( name = "phones" )
@Getter
@Setter
public class Phones {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column( name = "phoneId" , nullable = false )
    private Long phoneId ;

    @Column( name = "phoneUuid" , updatable = false , nullable = false , unique = true , length = 36 )
    private String phoneid = java.util.UUID.randomUUID().toString() ;

    @Column(name = "phone", nullable = false, unique = true)
    private String phone ;

    @Column( name = "phoneStatus" , nullable = false )
    private Long phoneStatus ;

}