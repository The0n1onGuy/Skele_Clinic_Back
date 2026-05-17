package com.nexushiscore.controllers.Patients;


import com.nexushiscore.beans.Patients.PatientsBean;
import com.nexushiscore.services.Patients.PatientsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

// @CrossOrigin( origins = "http://localhost:3000" , maxAge = 3600 )
@RestController
@RequestMapping( "/api/his/v1/patients/" )
public class PatientsController {

    @Autowired
    PatientsService patientsService;

    @GetMapping("patients-single")
    public PatientsBean getPatientsSingle() {
        PatientsBean a = new PatientsBean();
        a.setNombre("Juanito");
        a.setApellidos("Pérez");
        a.setDireccion("Villas Jotoch");
        a.setTelefono("9999999999");
        a.setFechaNacimiento(LocalDate.of(2005, 5, 20));
        return a;
    }

    @GetMapping("patients-list")
    public List<PatientsBean> getPatientsList() {
        return patientsService.getAllPatients();
    }

    @PostMapping("new")
    public ResponseEntity<?> nuevo(@Valid @RequestBody PatientsBean patientsBean) {
        return (ResponseEntity<?>) patientsService.nuevo(patientsBean);
    }
    @GetMapping("all")
    public List<PatientsBean> getAll() { // Cambiado a PatientsBean
        return patientsService.getAllPatients();
    }

    @PutMapping("/change/{id}")
    public ResponseEntity<?> updateData(@Valid @RequestBody PatientsBean bean, BindingResult result, @PathVariable UUID id  // ← Long → UUID
    ) {
        return patientsService.updateData(bean, result, id);
    }

}