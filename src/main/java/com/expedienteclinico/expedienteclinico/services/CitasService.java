package com.expedienteclinico.expedienteclinico.services;

import com.expedienteclinico.expedienteclinico.models.CitasModel;
import com.expedienteclinico.expedienteclinico.repositories.ICitasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CitasService {

    @Autowired
    private ICitasRepository iCitasRepository;

    public List<CitasModel> listarTodas() {
        return iCitasRepository.findAll();
    }

    public CitasModel guardarCita(CitasModel cita) {
        if (cita.getEstado() == null) cita.setEstado("PENDIENTE");
        return iCitasRepository.save(cita);
    }

    public void eliminarCita(Long id) {
        iCitasRepository.deleteById(id);
    }
}