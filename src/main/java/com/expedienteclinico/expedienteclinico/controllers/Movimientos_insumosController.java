package com.expedienteclinico.expedienteclinico.controllers;


import com.expedienteclinico.expedienteclinico.models.Movimientos_insumosModel;
import com.expedienteclinico.expedienteclinico.services.Movimientos_insumosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin( origins = "*" , maxAge = 3600 )
@RestController
@RequestMapping( "/movimiento_insumos-controller/" )
public class Movimientos_insumosController {

    @Autowired
    Movimientos_insumosService movimientos_insumosService ;

    @GetMapping( "all")
    public List<Movimientos_insumosModel> getAll() {
        return  movimientos_insumosService.getAll() ;
    }
}
