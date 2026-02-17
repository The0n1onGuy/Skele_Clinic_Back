package com.expedienteclinico.expedienteclinico.controllers;

import com.expedienteclinico.expedienteclinico.models.Empleados_LyRModel;
import com.expedienteclinico.expedienteclinico.services.Empleados_LyRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin( origins = "*" , maxAge = 3600 )
@RestController
@RequestMapping( "/ empleados_LyR-controller/" )
public class Empleados_LyRController {

    @Autowired
    Empleados_LyRService  empleados_LyRService ;

    @GetMapping( "all")
    public List<Empleados_LyRModel> getAll() {
        return  empleados_LyRService.getAll() ;
    }
}
