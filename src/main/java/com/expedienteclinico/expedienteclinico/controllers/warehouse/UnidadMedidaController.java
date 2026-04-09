package com.expedienteclinico.expedienteclinico.controllers.warehouse;

import com.expedienteclinico.expedienteclinico.models.warehouse.UnidadMedidaModel;
import com.expedienteclinico.expedienteclinico.payload.response.ResponseFactory;
import com.expedienteclinico.expedienteclinico.services.warehouse.UnidadMedidaService;
import com.expedienteclinico.expedienteclinico.services.common.ConnectivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/warehouse")
@Tag(name = "Warehouse: Units of Measure", description = "Management of item measurement units like Boxes, Pieces, and Kits")
@CrossOrigin(origins = "*")
public class UnidadMedidaController {

    @Autowired
    private UnidadMedidaService unitService;
    private ConnectivityService connectivityService;

    @Operation(summary = "Get all units", description = "Returns a list of all measurement units defined in the system.")
    @GetMapping
    public List<UnidadMedidaModel> getAll() {
        return unitService.getAll();
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

        // Assegurar el renderCreated que es el que se usa en responsefactory
        return ResponseFactory.renderCreated("Unidad creada con éxito", saved);
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

    @GetMapping("/health")
    public ResponseEntity<?> healthCheck() {
        Map<String, Object> connectivity = connectivityService.checkFullConnectivity();
        boolean isUp = connectivity.get("status").equals("STABLE");

        // Si el internet es inestable o nulo, mandamos el reporte detallado
        return ResponseFactory.renderSuccess("Estado de infraestructura", connectivity);
    }
}