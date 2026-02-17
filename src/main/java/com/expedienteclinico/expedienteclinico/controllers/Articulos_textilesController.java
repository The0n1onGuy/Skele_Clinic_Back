package com.expedienteclinico.expedienteclinico.controllers;

import com.expedienteclinico.expedienteclinico.models.Articulos_textilesModel;
import com.expedienteclinico.expedienteclinico.services.Articulos_textilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin( origins = "*" , maxAge = 3600 )
@RestController
@RequestMapping( "/categoria_textiles-controller/" )
public class Articulos_textilesController {

    @Autowired
    Articulos_textilesService Articulos_textilesService;

    @GetMapping( "all")
    public List<Articulos_textilesModel> getAll() {
        return Articulos_textilesService.getAll() ;
    }
}
