package com.expedienteclinico.expedienteclinico.controllers.Patients;


import com.expedienteclinico.expedienteclinico.beans.Patients.PatientsBean;
import com.expedienteclinico.expedienteclinico.services.Patients.PatientsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pacientes")
@CrossOrigin(origins = "*") // Importante para desarrollo frontend
public class PatientsController {
    @Autowired
    private PatientsService service;

    @PostMapping("/nuevo")
    public ResponseEntity<?> crear(@RequestBody PatientsBean bean) {
        return ResponseEntity.ok(service.registrarPaciente(bean));
    }
}