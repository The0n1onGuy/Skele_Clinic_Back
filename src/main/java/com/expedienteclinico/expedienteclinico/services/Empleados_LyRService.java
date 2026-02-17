package com.expedienteclinico.expedienteclinico.services;


import com.expedienteclinico.expedienteclinico.models.Empleados_LyRModel;
import com.expedienteclinico.expedienteclinico.repositories.IEmpleados_LyRRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class Empleados_LyRService {

    @Autowired
    IEmpleados_LyRRepository iEmpleadosLyRRepository;

    public List<Empleados_LyRModel> getAll() {
        return iEmpleadosLyRRepository.findAll() ;
    }
}
