package com.nexushiscore.controllers.warehouse; // Paquete corregido

import com.nexushiscore.models.warehouse.UnidadMedidaModel;
import com.nexussharedcore.payload.response.ResponseFactory; // Paquete corregido
import com.nexushiscore.services.warehouse.UnidadMedidaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/warehouse")
@Tag(name = "Warehouse: Units of Measure", description = "Management of item measurement units like Boxes, Pieces, and Kits")
@CrossOrigin(origins = "*")
public class UnidadMedidaController {

    @Autowired
    private UnidadMedidaService unitService;

    // ELIMINADA la inyección de ConnectivityService

    @Operation(summary = "Get all units", description = "Returns a list of all measurement units defined in the system.")
    @GetMapping
    public ResponseEntity<?> getAll() {
        List<UnidadMedidaModel> units = unitService.getAll();
        // Estandarización usando la fábrica compartida
        return ResponseFactory.successMessage("Unidades obtenidas con éxito", units);
    }

    @Operation(summary = "Create a new unit", description = "Saves a new measurement unit (e.g., 'Box' or 'Piece') into the database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Unit created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody UnidadMedidaModel model, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseFactory.renderValidationError(result);
        }
        UnidadMedidaModel saved = unitService.save(model);

        // Uso del método actualizado de la fábrica de respuestas (antes renderCreated)
        return ResponseFactory.created("Unidad creada con éxito", saved);
    }

    @Operation(summary = "Get unit by ID", description = "Searches for a specific unit using its unique ID. Returns 404 if not found.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Unit found and returned successfully"),
            @ApiResponse(responseCode = "404", description = "The provided ID does not exist in the database")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UnidadMedidaModel> getById(
            @Parameter(description = "ID of the unit to be retrieved", example = "1")
            @PathVariable Long id) {

        return unitService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ELIMINADO el endpoint /health
}