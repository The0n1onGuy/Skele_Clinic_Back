package com.expedienteclinico.expedienteclinico.controllers.rpbi;

import com.expedienteclinico.expedienteclinico.beans.rpbi.RpbiClasificacionObject;
import com.expedienteclinico.expedienteclinico.services.rpbi.RpbiClasificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rpbi/clasificacion") // ruta base del módulo
// @CrossOrigin( origins = "*" , maxAge = 3600 ) // Para pruebas (?) xd
public class RpbiClasificacionController {

    @Autowired
    private RpbiClasificacionService service;

    @GetMapping("/all")
    public ResponseEntity<List<RpbiClasificacionObject>> getAll() {
        // llamada al servicio y retorno de un HTTP 200 (el ok) con la lista
        return ResponseEntity.ok(service.getAllClasificaciones());
                            // ↑ Esta cosa
    }
}