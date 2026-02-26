package com.expedienteclinico.expedienteclinico.models;
//Si documentas el modelo, Swagger mostrará ejemplos automáticos en la sección de Schemas (abajo en la web).
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class StatusModel {
    @Schema(description = "Identificador único en la base de datos", example = "101")
    private Long id;

    @Schema(description = "Nombre descriptivo del estado", example = "ACTIVO", allowableValues = {"ACTIVO", "INACTIVO", "PENDIENTE"})
    private String nombre;
}
