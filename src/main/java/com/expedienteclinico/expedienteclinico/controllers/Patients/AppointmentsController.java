package com.expedienteclinico.expedienteclinico.controllers.Patients;

import com.expedienteclinico.expedienteclinico.beans.Patients.AppointmentsBean;
import com.expedienteclinico.expedienteclinico.models.Patients.AppointmentsModel;
import com.expedienteclinico.expedienteclinico.services.Patients.AppointmentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@CrossOrigin( origins = "http://localhost:3000" , maxAge = 3600 )
@RestController
@RequestMapping( "/appointments-controller/" )
public class AppointmentsController {

    @Autowired
    AppointmentsService appointmentsService ;

    @GetMapping( "appointments-single" )
    public AppointmentsBean getAppointmentsSingle() {
        AppointmentsBean a = new AppointmentsBean() ;
        a.setConsultorio( "Consultorio 101" ) ;
        return a ;
    }

    @GetMapping( "appointments-list" )
    public List<AppointmentsBean> getAppointmentsList() { // <-- Cambiado a List
        return appointmentsService.getAllAppointments() ;
    }
    @PostMapping( "new" )
    public Object nuevo( @Valid @RequestBody AppointmentsBean appointmentsBean ) {
        return appointmentsService.nuevo( appointmentsBean ) ;
    }

    @GetMapping( "all" )
    public List< AppointmentsModel > getAll() {
        return appointmentsService.getAll() ;
    }

    @PutMapping ( "change/{id}" )
    public ResponseEntity < ? > updateData (@Valid @RequestBody AppointmentsBean appointmentsBean , BindingResult result, @PathVariable UUID id ) {
        return appointmentsService.updateData(appointmentsBean, result, id);
    }
}