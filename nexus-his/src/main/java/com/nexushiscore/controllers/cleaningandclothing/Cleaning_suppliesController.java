package com.nexushiscore.controllers.cleaningandclothing;

import com.nexushiscore.beans.cleaningandclothing.Cleaning_suppliesObject;
import com.nexushiscore.services.cleaningandclothing.Cleaning_suppliesService;
import com.nexussharedcore.payload.response.ResponseFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController

// Ruta base del controlador
@RequestMapping("/api/lyr/supplies")
public class Cleaning_suppliesController {

    private final Cleaning_suppliesService cleaningSuppliesService;

    // 1. INYECCIÓN LIMPIA: Solo inyectamos lógica de negocio, nada de infraestructura
    public Cleaning_suppliesController(Cleaning_suppliesService cleaningSuppliesService) {
        this.cleaningSuppliesService = cleaningSuppliesService;
    }

    // Endpoint GET para obtener todos los insumos
    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        List<Cleaning_suppliesObject> supplies = cleaningSuppliesService.getAll();

        if (supplies == null || supplies.isEmpty()) {
            // 2. ESTÁNDAR NATIVO: Sin bases de datos, devolvemos 204 No Content directamente
            return ResponseEntity.noContent().build();
        }

        // 3. USO ESTÁTICO: Llamamos a la librería compartida
        return ResponseFactory.successMessage("Información obtenida con éxito", supplies);
    }
}