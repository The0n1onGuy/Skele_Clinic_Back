package com.nexuscore.controllers.cleaningandclothing;

import com.nexuscore.beans.cleaningandclothing.Cleaning_suppliesObject;
import com.nexuscore.payload.response.ApiResponsefactory;
import com.nexuscore.services.cleaningandclothing.Cleaning_suppliesService;
import com.nexuscore.services.system.HttpStatusService;
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

    // Servicio de lógica de negocio
    private final Cleaning_suppliesService cleaningSuppliesService;

    // Fábrica de respuestas
    private final ApiResponsefactory apiResponseFactory;

    // Servicio de códigos HTTP
    private final HttpStatusService httpStatusService;

    // Constructor con inyección de dependencias
    public Cleaning_suppliesController(Cleaning_suppliesService cleaningSuppliesService,
                                       ApiResponsefactory apiResponseFactory,
                                       HttpStatusService httpStatusService) {
        this.cleaningSuppliesService = cleaningSuppliesService;
        this.apiResponseFactory = apiResponseFactory;
        this.httpStatusService = httpStatusService;
    }

    // Endpoint GET para obtener todos los insumos
    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAll() {

        // Obtiene la lista desde el servicio
        List<Cleaning_suppliesObject> supplies = cleaningSuppliesService.getAll();

        if (supplies == null || supplies.isEmpty()) {
            return ResponseEntity
                    .status(httpStatusService.getSpringStatus(204))
                    .build();
        }

        return ResponseEntity
                .status(httpStatusService.getSpringStatus(200))
                .body(apiResponseFactory.success(supplies));
    }
}