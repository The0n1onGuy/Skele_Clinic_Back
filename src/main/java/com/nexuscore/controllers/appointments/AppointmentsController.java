package com.nexuscore.controllers.appointments;

import com.nexuscore.models.appointments.AppointmentsModel;
import com.nexuscore.services.appointments.AppointmentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/his/v1/appointments")
// @CrossOrigin(origins = "*")
public class AppointmentsController {

    @Autowired
    private AppointmentsService appointmentsService;

    @GetMapping("datos")
    public List<AppointmentsModel> obtenerCitas() {
        return appointmentsService.listarTodas();
    }

    @PostMapping("create")
    public AppointmentsModel crearCita(@RequestBody AppointmentsModel cita) {
        return appointmentsService.guardarCita(cita);
    }

    @DeleteMapping("/{id}")
    public void borrarCita(@PathVariable Long id) {
        appointmentsService.eliminarCita(id);
    }

    @GetMapping("/paciente/{name}")
    public List<AppointmentsModel> obtenerCitasPorPaciente(@PathVariable("name") String name) {
        return appointmentsService.buscarPorNombrePaciente(name);
    }

    @PutMapping("/{id}")
    public AppointmentsModel modificarCita(@PathVariable Long id, @RequestBody AppointmentsModel cita) {
        return appointmentsService.actualizarCita(id, cita);
    }
}