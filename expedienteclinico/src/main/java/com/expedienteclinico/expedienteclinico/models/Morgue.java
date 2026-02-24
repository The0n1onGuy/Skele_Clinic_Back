package com.expedienteclinico.expedienteclinico.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Table(name = "morgue")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Morgue {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    private Long id;
//referencia a pacientes
    @OneToOne
    @JoinColumn(name = "id_paciente", referencedColumnName = "id")
    private FirstModel paciente; //cambiar
    private String causa_defuncion;
    private String fecha_ingreso;

    @ManyToOne
    @JoinColumn (name = "id_status" , nullable = false)
    private StatusModel idStatus;
    private Integer numero_gaveta;

    // pruebas mock
    private Long idPacienteLocal; // Usaremos esto para simular el ID de Natalia
    private String nombrePacienteSimulado; // Para ver un nombre en las pruebas
}