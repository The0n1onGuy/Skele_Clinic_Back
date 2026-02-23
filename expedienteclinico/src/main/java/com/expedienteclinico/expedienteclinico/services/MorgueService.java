package com.expedienteclinico.expedienteclinico.services;

import com.expedienteclinico.expedienteclinico.beans.MorgueObject;
import com.expedienteclinico.expedienteclinico.models.Morgue;
import com.expedienteclinico.expedienteclinico.repositories.IMorgueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class MorgueService {

    @Autowired
    IMorgueRepository iMorgueRepository;

    public ArrayList<Object> getMorgueList() {
        ArrayList<Object> listaPrueba = new ArrayList<>();
        // Datos de ejemplo para pruebas locales
        listaPrueba.add(new MorgueObject("Juan Pérez", 45, "Infarto", "2026-02-18", 1));
        listaPrueba.add(new MorgueObject("María López", 70, "Causas Naturales", "2026-02-18", 2));
        return listaPrueba;
    }

    public List<Morgue> getAll() {
        return iMorgueRepository.findAll();
    }
}