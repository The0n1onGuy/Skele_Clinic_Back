package com.nexushiscore.controllers.Patients;

import com.nexushiscore.beans.Patients.TriageBean;
import com.nexushiscore.models.patients.TriageModel;
import com.nexushiscore.services.Patients.TriageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

// @CrossOrigin( origins = "http://localhost:3000", maxAge = 3600 )
@RestController
@RequestMapping( "api/his/v1/triage-controller/" )
public class TriageController {

    @Autowired
    TriageService triageService ;

    @GetMapping( "triage-single" )
    public TriageBean getTriageSingle() {
        TriageBean a = new TriageBean() ;
        a.setTemperatura( 38.5 ) ;
        a.setPresionArterial( "130/90" ) ;
        a.setFrecuenciaCardiaca( 95 ) ;
        return a ;
    }

    @GetMapping( "triage-list" )
    public List<TriageBean> getTriageList() { // <-- Cambiado a List
        // Nota: Asegúrate de que el método en tu servicio se llame getAllTriage() o getAllTriages()
        return triageService.getAllTriage() ;
    }
    @PostMapping("new")
    public Object nuevo(@Valid @RequestBody TriageBean triageBean) {
        return triageService.nuevo(triageBean); // Aquí es donde "usas" el método
    }

    @GetMapping( "all" )
    public List<TriageModel> getAll() {
        return triageService.getAll() ;
    }

    @PutMapping ( "change/{id}" )
    public ResponseEntity < ? > updateData (@Valid @RequestBody TriageBean triageBean, BindingResult result, @PathVariable UUID id ) {
        return triageService.updateData(triageBean, result, id);
    }
}