package com.nexushiscore.models.cleaningandclothing;//package com.expedienteclinico.expedienteclinico.models.cleaningandclothing;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import java.time.LocalDateTime;
//import java.util.UUID;
//
//@Entity
//@Table(name = "tx_textile_movements")
//@NoArgsConstructor
//@AllArgsConstructor
//@Getter
//@Setter
//public class Textile_movementsModel {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(nullable = false)
//    private Long movementId;
//
//    @Column(name = "uuid", updatable = false, nullable = false, unique = true, length = 36)
//    private String uuid = UUID.randomUUID().toString();
//
//    @Column(name = "movement_type", nullable = false, length = 50)
//    private String movementType; // (Entrada, Salida, Lavado, Baja)
//
//    @Column(nullable = false)
//    private Integer amount;
//
//    @Column(name = "movement_date", nullable = false)
//    private LocalDateTime movementDate;
//
//    @Column(length = 255)
//    private String observations;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "article_id", nullable = false)
//    private Textile_articlesModel textileArticle;
//
//    @ManyToOne
//    @JoinColumn(name = "employees_id", nullable = false)
//    private LyR_EmployeesModel employees;
//}