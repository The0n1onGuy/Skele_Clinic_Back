package com.nexuscore.models.warehouse;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

@Entity
@Table(name = "warehouse_units_measure")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Model representing measurement units for warehouse items")
public class UnidadMedidaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the unit", example = "1")
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 20, message = "El nombre no puede exceder los 20 caracteres")
    @Column(nullable = false, length = 20)
    @Schema(description = "Full name of the unit", example = "Box")
    private String name;

    @NotBlank(message = "La abreviación no puede estar vacía")
    @Size(max = 5, message = "La abreviación no puede exceder los 5 caracteres")
    @Column(nullable = false, length = 5)
    @Schema(description = "Short abbreviation", example = "BX")
    private String abbreviation;
}