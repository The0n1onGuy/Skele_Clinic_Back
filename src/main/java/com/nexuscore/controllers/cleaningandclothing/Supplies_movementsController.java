package com.nexuscore.controllers.cleaningandclothing;

import com.nexuscore.beans.cleaningandclothing.Supplies_movementsObject;
import com.nexuscore.payload.response.ApiResponsefactory;
import com.nexuscore.services.cleaningandclothing.Supplies_movementsService;
import com.nexuscore.services.system.HttpStatusService;
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
    private final ApiResponsefactory apiResponseFactory;
    private final HttpStatusService httpStatusService;

    public Supplies_movementsController(Supplies_movementsService suppliesMovementsService,
                                        ApiResponsefactory apiResponseFactory,
                                        HttpStatusService httpStatusService) {
        this.suppliesMovementsService = suppliesMovementsService;
        this.apiResponseFactory = apiResponseFactory;
        this.httpStatusService = httpStatusService;
    }

    /**
     * Obtener todos los movimientos de insumos
     */
    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAll() {

        List<Supplies_movementsObject> movements = suppliesMovementsService.getAll();

        if (movements == null || movements.isEmpty()) {
            return ResponseEntity
                    .status(httpStatusService.getSpringStatus(204))
                    .build();
        }

        return ResponseEntity
                .status(httpStatusService.getSpringStatus(200))
                .body(apiResponseFactory.success(movements));
    }
}