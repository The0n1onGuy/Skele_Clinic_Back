package com.nexuscore.controllers.cleaningandclothing;//package com.expedienteclinico.expedienteclinico.controllers.cleaningandclothing;
//
//import com.expedienteclinico.expedienteclinico.beans.cleaningandclothing.Textile_movementsObject;
//import com.expedienteclinico.expedienteclinico.payload.response.ApiResponsefactory;
//import com.expedienteclinico.expedienteclinico.services.cleaningandclothing.Textile_movementsService;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/lyr/textile-movements")
//public class Textile_movementsController {
//
//    private final Textile_movementsService textileMovementsService;
//    private final ApiResponsefactory apiResponseFactory;
//
//    public Textile_movementsController(Textile_movementsService textileMovementsService,
//                                       ApiResponsefactory apiResponseFactory) {
//        this.textileMovementsService = textileMovementsService;
//        this.apiResponseFactory = apiResponseFactory;
//    }
//
//    /**
//     * Obtener todos los movimientos de artículos textiles
//     */
//    @GetMapping
//    public ResponseEntity<Map<String, Object>> getAll() {
//
//        List<Textile_movementsObject> movements = textileMovementsService.getAll();
//
//        if (movements.isEmpty()) {
//            return ResponseEntity.ok(
//                    apiResponseFactory.successMessage("No hay movimientos de artículos textiles registrados", movements)
//            );
//        }
//
//        return ResponseEntity.ok(
//                apiResponseFactory.success(movements)
//        );
//    }
//}