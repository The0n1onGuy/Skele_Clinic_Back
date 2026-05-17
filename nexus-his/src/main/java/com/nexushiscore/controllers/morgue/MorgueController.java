package com.nexushiscore.controllers.morgue;

import com.nexushiscore.models.morgue.Morgue;
import com.nexushiscore.services.morgue.MorgueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

// @CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/his/v1/morgue-controller/")
public class MorgueController {

    @Autowired
    MorgueService morgueService;

    @GetMapping("morgue-list")
    public ArrayList<Object> getMorgueList() {
        return morgueService.getMorgueList();
    }

    @GetMapping("all")
    public List<Morgue> getAll() {
        return morgueService.getAll();
    }

    // /morgue-controller/buscar-paciente/id de coffe
    @GetMapping("buscar-paciente/{id}")
    public Morgue getByPaciente(@PathVariable Long id) {
        // pide al servicio que busque al fallecido vinculado a ese ID de paciente
        return morgueService.buscarPorPaciente(id);
    }
    //prueba mck
    @GetMapping("buscar-paciente-local/{id}")
    public Morgue buscarLocal(@PathVariable Long id) {
        return morgueService.buscarPorPacienteLocal(id);
    }
}