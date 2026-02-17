package com.expedienteclinico.expedienteclinico.services;


import com.expedienteclinico.expedienteclinico.models.Movimientos_insumosModel;
import com.expedienteclinico.expedienteclinico.models.Movimientos_textilesModel;
import com.expedienteclinico.expedienteclinico.repositories.IMovimientos_insumosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Movimientos_insumosService {

    @Autowired
    IMovimientos_insumosRepository iMovimientos_insumosRepository;

    public List<Movimientos_insumosModel> getAll() {
        return iMovimientos_insumosRepository.findAll();
    }
}
