package com.expedienteclinico.expedienteclinico.services;


import com.expedienteclinico.expedienteclinico.models.Insumo_limpiezaModel;
import com.expedienteclinico.expedienteclinico.repositories.IInsumo_limpiezaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Insumos_limpiezaService {

    @Autowired
    IInsumo_limpiezaRepository iInsumo_limpiezaRepository;

    public List<Insumo_limpiezaModel> getAll() {
        return iInsumo_limpiezaRepository.findAll() ;
    }
}
