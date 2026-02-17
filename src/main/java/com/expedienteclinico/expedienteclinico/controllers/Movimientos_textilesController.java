package com.expedienteclinico.expedienteclinico.controllers;


import com.expedienteclinico.expedienteclinico.models.Movimientos_textilesModel;
import com.expedienteclinico.expedienteclinico.services.Movimientos_textilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin( origins = "*" , maxAge = 3600 )
@RestController
@RequestMapping( "/movimiento_textiles-controller/" )
public class Movimientos_textilesController {

    @Autowired
    Movimientos_textilesService movimientos_textilesService ;

    @GetMapping( "all")
    public List<Movimientos_textilesModel> getAll() {
        return  movimientos_textilesService.getAll() ;
    }
}
