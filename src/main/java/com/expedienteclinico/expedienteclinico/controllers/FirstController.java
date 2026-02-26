package com.expedienteclinico.expedienteclinico.controllers;

import com.expedienteclinico.expedienteclinico.beans.FirstObject;
import com.expedienteclinico.expedienteclinico.models.FirstModel;
import com.expedienteclinico.expedienteclinico.services.FirstService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

//Organizacion de EndPoints (usaremos Tag)
import io.swagger.v3.oas.annotations.tags.Tag;
//Usa @Operation para explicar el propósito de cada metodo y @ApiResponse para documentar los posibles errores
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
@Deprecated
@CrossOrigin( origins = "*" , maxAge = 3600 )
@RestController
@RequestMapping( "/first-controller/" )
@Tag(name = "Módulo de Pruebas", description = "Endpoints iniciales para validación del sistema")
public class FirstController {

    @Autowired
    FirstService firstService ;

    @GetMapping( "first-single" )
    public FirstObject getFirstSingle() {
        FirstObject a = new FirstObject() ;
            a.setNombre( "Juanito" ) ;
            a.setDireccion( "Villas Jotoch" ) ;
            a.setEdad( 21 ) ;
            a.setTelefono( "9999999999" ) ;

        return a ;
    }
    @Operation(summary = "Obtener lista completa", description = "Consulta la base de datos y retorna todos los registros de estado actuales.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista recuperada con éxito"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al consultar la base de datos")
    })
    @GetMapping( "first-list" )
    public ArrayList< Object > getFirstList() {
        return firstService.getFirstList() ;
    }

    @PostMapping( "new" )
    public Object nuevo( @RequestBody FirstObject firstObject ) {
        return firstService.nuevo( firstObject ) ;
    }




    @GetMapping( "fibonacci" )
    public void fibonacci() {

        System.out.println("Serie de Fibonacci" ) ;
        firstService.fibonacci(50);

    }


    @GetMapping( "all" )
    public List< FirstModel > getAll() {
        return firstService.getAll() ;
    }

    @PutMapping("change/{id}")
    public ResponseEntity<?> updateData(@Valid @RequestBody FirstModel firstModel, BindingResult result, @PathVariable Long id){
        return firstService.updateData(firstModel, result, id);
    }


}
