package com.expedienteclinico.expedienteclinico.controllers;

import com.expedienteclinico.expedienteclinico.models.CitasModel;
import com.expedienteclinico.expedienteclinico.services.CitasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/citas/")
@CrossOrigin(origins = "*")
public class CitasController {

    @Autowired
    private CitasService citasService;

    @GetMapping("datos")
    public List<CitasModel> obtenerCitas() {
        return citasService.listarTodas();
    }

    @PostMapping
    public CitasModel crearCita(@RequestBody CitasModel cita) {
        return citasService.guardarCita(cita);
    }

    @DeleteMapping("/{id}")
    public void borrarCita(@PathVariable Long id) {
        citasService.eliminarCita(id);
    }
}