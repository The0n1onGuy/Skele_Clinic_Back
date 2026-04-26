package com.nexushiscore.models.cleaningandclothing;//package com.expedienteclinico.expedienteclinico.models.cleaningandclothing;
//
//import com.expedienteclinico.expedienteclinico.models.system.StatusModel;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import jakarta.persistence.*;
//
//import java.util.UUID;
//
//@Entity
//@Table(name = "cat_textile_articles")
//@NoArgsConstructor
//@AllArgsConstructor
//@Getter
//@Setter
//public class Textile_articlesModel {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(nullable = false)
//    private Long articlesId;
//
//    @Column(name = "uuid", updatable = false, nullable = false, unique = true, length = 36)
//    private String uuid = UUID.randomUUID().toString();
//
//    @Column(nullable = false, length = 100)
//    private String name;
//
//    @Column(length = 255)
//    private String description;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "status_id", nullable = false)
//    private StatusModel status;
//}

