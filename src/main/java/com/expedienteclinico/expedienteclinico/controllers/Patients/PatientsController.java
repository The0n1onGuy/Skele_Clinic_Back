package com.expedienteclinico.expedienteclinico.controllers.Patients;

import com.expedienteclinico.expedienteclinico.beans.Patients.PatientsBean;
import com.expedienteclinico.expedienteclinico.services.Patients.PatientsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.time.LocalDate;

@CrossOrigin( origins = "http://localhost:3000" , maxAge = 3600 )
@RestController
@RequestMapping( "/patients-controller/" )
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
    public Object nuevo(@Valid @RequestBody PatientsBean patientsBean) {
        return patientsService.nuevo(patientsBean);
    }

    @GetMapping("all")
    public List<PatientsBean> getAll() { // Cambiado a PatientsBean
        return patientsService.getAllPatients();
    }

    @PutMapping("change/{id}")
    public ResponseEntity<?> updateData(@Valid @RequestBody PatientsBean patientsBean, BindingResult result, @PathVariable Long id) {
        // Le pasamos el Bean al servicio
        return patientsService.updateData(patientsBean, result, id);
    }
}