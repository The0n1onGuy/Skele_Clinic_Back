package com.nexushiscore.controllers.cleaningandclothing;

import com.nexushiscore.beans.cleaningandclothing.Supplies_movementsObject;
import com.nexushiscore.services.cleaningandclothing.Supplies_movementsService;
import com.nexussharedcore.payload.response.ResponseFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/lyr/supplies-movements")
public class Supplies_movementsController {

    private final Supplies_movementsService suppliesMovementsService;

    // 1. INYECCIÓN LIMPIA: Solo inyectamos lógica de negocio, nada de infraestructura
    public Supplies_movementsController(Supplies_movementsService suppliesMovementsService) {
        this.suppliesMovementsService = suppliesMovementsService;
    }

    /**
     * Obtener todos los movimientos de insumos
     */
    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAll() {

        List<Supplies_movementsObject> movements = suppliesMovementsService.getAll();

        if (movements == null || movements.isEmpty()) {
            // 2. ESTÁNDAR NATIVO: Sin bases de datos, devolvemos 204 No Content directamente
            return ResponseEntity.noContent().build();
        }

        // 3. USO ESTÁTICO: Llamamos a la librería compartida
        return ResponseFactory.successMessage("Información obtenida con éxito", movements);
    }
}