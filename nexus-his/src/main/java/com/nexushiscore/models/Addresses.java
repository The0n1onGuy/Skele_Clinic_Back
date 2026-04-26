package com.nexushiscore.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table( name = "addresses" )
@Getter
@Setter
public class Addresses {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column( name = "addressId" , nullable = false )
    private Long addressId ;

    @Column( name = "addressUuid" , updatable = false , nullable = false , unique = true , length = 36 )
    private String addressUuid = java.util.UUID.randomUUID().toString() ;

    @Column(name = "address", nullable = false, unique = true)
    private String address ;

    @Column( name = "addressStatus" , nullable = false )
    private Long addressStatus ;
}