package com.expedienteclinico.expedienteclinico.models;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

@Entity
//@Table(appliesTo = "Departments")
@Table (name = "departments")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Departments {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "UUID" , nullable = false, length = 36)
    private Character UUID;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    private String dep_name;
}
