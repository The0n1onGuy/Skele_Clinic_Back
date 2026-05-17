package com.nexushiscore.controllers.cleaningandclothing;//package com.expedienteclinico.expedienteclinico.controllers.cleaningandclothing;
//
//import com.expedienteclinico.expedienteclinico.beans.cleaningandclothing.Textile_articlesObject;
//import com.expedienteclinico.expedienteclinico.payload.response.ApiResponsefactory;
//import com.expedienteclinico.expedienteclinico.services.cleaningandclothing.Textile_articlesService;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/lyr/textile-articles")
//public class Textile_articlesController {
//
//    private final Textile_articlesService textileArticlesService;
//    private final ApiResponsefactory apiResponseFactory;
//
//    public Textile_articlesController(Textile_articlesService textileArticlesService,
//                                      ApiResponsefactory apiResponseFactory) {
//        this.textileArticlesService = textileArticlesService;
//        this.apiResponseFactory = apiResponseFactory;
//    }
//
//    /**
//     * Obtener todos los artículos textiles
//     */
//    @GetMapping
//    public ResponseEntity<Map<String, Object>> getAll() {
//
//        List<Textile_articlesObject> articles = textileArticlesService.getAll();
//
//        if (articles.isEmpty()) {
//            return ResponseEntity.ok(
//                    apiResponseFactory.successMessage("No hay artículos textiles registrados", articles)
//            );
//        }
//
//        return ResponseEntity.ok(
//                apiResponseFactory.success(articles)
//        );
//    }
//}