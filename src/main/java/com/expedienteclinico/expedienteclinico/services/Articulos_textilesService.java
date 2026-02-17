package com.expedienteclinico.expedienteclinico.services;


import com.expedienteclinico.expedienteclinico.models.Articulos_textilesModel;
import com.expedienteclinico.expedienteclinico.repositories.IArticulos_textilesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Articulos_textilesService {

    @Autowired
    IArticulos_textilesRepository iArticulos_textilesRepository;

    public List<Articulos_textilesModel> getAll() {
        return iArticulos_textilesRepository.findAll() ;
    }
}
