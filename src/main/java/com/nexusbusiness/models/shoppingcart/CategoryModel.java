package com.nexusbusiness.models.shoppingcart;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "pos_category")
@Getter
@Setter
public class CategoryModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false) // Buena práctica: nombrar explícitamente la PK
    @Schema(description = "Identificador único en la base de datos", example = "101")
    private Long id;


    @Column(nullable = false, unique = true, length = 100 )
    @Schema(description = "Nombre descriptivo del estado")
    private String name;

    @Lob
    private String description;
}
