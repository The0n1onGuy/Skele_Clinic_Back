package com.expedienteclinico.expedienteclinico.controllers;

import com.expedienteclinico.expedienteclinico.beans.FirstObject;
import com.expedienteclinico.expedienteclinico.models.FirstModel;
import com.expedienteclinico.expedienteclinico.services.FirstService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


@CrossOrigin (origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/first-controller/")

//CONTROLADOR
public class FirstController {
    @Autowired
    FirstService firstService;

    @GetMapping("first-single")
    public FirstObject getFirstSingle(){
        FirstObject a = new FirstObject();
        a.setNombre("Pech");
        a.setDireccion("Villas otoch");
        a.setEdad( 21 );
        a.setTelefono("9999999999");
        return a;
    }
    @GetMapping("first-list")
    public ArrayList <Object> getFirstList(){
        return firstService.getFirstList();
    }
    @PostMapping("new")
    public Object nuevo(@RequestBody FirstObject firstObject){
        return  firstService.nuevo(firstObject);
    }

    @GetMapping("all")
    public List<FirstModel> getAll(){
        return firstService.getAll();
    }

    @PutMapping("change/{id}")
    public ResponseEntity<?> updateData(@Valid @RequestBody FirstModel firstModel, BindingResult result, @PathVariable Long id){
        return firstService.updateData(firstModel, result, id);
    }
}
