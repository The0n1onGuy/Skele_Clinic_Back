package com.expedienteclinico.expedienteclinico.services;


import com.expedienteclinico.expedienteclinico.models.Movimientos_textilesModel;
import com.expedienteclinico.expedienteclinico.repositories.IMovimientos_textilesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class Movimientos_textilesService {

    @Autowired
    IMovimientos_textilesRepository iMovimientosTextilesRepository;

    public List<Movimientos_textilesModel> getAll() {
        return iMovimientosTextilesRepository.findAll();
    }
}
