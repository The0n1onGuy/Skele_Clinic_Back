package com.nexuscore.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table( name = "emails" )
@Getter
@Setter
public class EMails {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column( name = "emailId" , nullable = false )
    private Long emailId ;

    @Column( name = "eMailUuid" , updatable = false , nullable = false , unique = true , length = 36 )
    private String eMailUuid = java.util.UUID.randomUUID().toString() ;

    @Column(name = "email", nullable = false, unique = true)
    private String eMail ;

    @Column( name = "emailStatus" , nullable = false )
    private Long emailStatus ;
}