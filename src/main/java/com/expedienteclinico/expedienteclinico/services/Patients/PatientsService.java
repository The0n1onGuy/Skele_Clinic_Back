package com.expedienteclinico.expedienteclinico.services.Patients;

import com.expedienteclinico.expedienteclinico.beans.Patients.PatientsBean;
import com.expedienteclinico.expedienteclinico.models.Patients.PatientsModel;
import com.expedienteclinico.expedienteclinico.repositories.Patients.IPatientsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PatientsService {


    @Autowired
    private IPatientsRepository repo;

    public PatientsModel registrarPaciente(PatientsBean bean) {
        // Validar si existe CURP...
        PatientsModel p = new PatientsModel();
        // Mapear datos del bean al entity
        p.setNombre(bean.getNombre());
        // ...
        return repo.save(p);
    }
}