package com.nexushiscore.models.warehouse;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "categories")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Schema(description = "Model representing the category of items in the warehouse (Pharmacy, Clinic or Medical Equipment)")
public class CategoryModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique category identifier", example = "1")
    private Long id;

    @Column(length = 50, nullable = false)
    @Schema(description = "Category name", example = "Medical Equipment", allowableValues = {"Pharmacy", "Healing Materials", "Medical Equipment"})
    private String name;

    @Column(columnDefinition = "TEXT")
    @Schema(description = "Detailed description of what this category includes", example = "All specialised machines and medical tools.")
    private String description;


}