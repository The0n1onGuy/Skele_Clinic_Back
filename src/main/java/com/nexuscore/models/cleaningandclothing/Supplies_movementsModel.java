package com.nexuscore.models.cleaningandclothing;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "supplies_movements")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Supplies_movementsModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_movimiento", nullable = false)
    private Long Id_motion;

    @Column(name = "uuid", updatable = false, nullable = false, unique = true, length = 36)
    private String uuid = UUID.randomUUID().toString();


    @Column(name = "tipo_movimiento")
    private String movementType;

    @Column(name = "cantidad")
    private Integer amount;

    @Column(name = "fecha_movimiento")
    private LocalDateTime movementDate;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observations;

    // 🔥 Nombre debe coincidir con el repository: supply
    @ManyToOne
    @JoinColumn(name = "supplies_id", nullable = false)
    private Cleaning_suppliesModel supply;

    @ManyToOne
    @JoinColumn(name = "employees_id", nullable = false)
    private LyR_EmployeesModel employee;
}