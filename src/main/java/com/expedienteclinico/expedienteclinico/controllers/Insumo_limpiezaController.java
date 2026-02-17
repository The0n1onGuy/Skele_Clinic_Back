package com.expedienteclinico.expedienteclinico.controllers;

import com.expedienteclinico.expedienteclinico.models.Insumo_limpiezaModel;
import com.expedienteclinico.expedienteclinico.services.Insumos_limpiezaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin( origins = "*" , maxAge = 3600 )
@RestController
@RequestMapping( "/insumo_limpieza-controller/" )
public class Insumo_limpiezaController {

    @Autowired
    Insumos_limpiezaService insumoLimpiezaService;

    @GetMapping( "all")
    public List<Insumo_limpiezaModel> getAll() {
        return insumoLimpiezaService.getAll() ;
    }
}
