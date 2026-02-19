package com.expedienteclinico.expedienteclinico.services.rpbi;

import com.expedienteclinico.expedienteclinico.beans.rpbi.RpbiEstadoFisicoObject;
import com.expedienteclinico.expedienteclinico.repositories.rpbi.IRpbiEstadoFisicoRepository;
import com.expedienteclinico.expedienteclinico.models.rpbi.RpbiEstadoFisicoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RpbiEstadoFisicoService {

    @Autowired
    private IRpbiEstadoFisicoRepository repository;

    public List<RpbiEstadoFisicoObject> getAll(){

        List<RpbiEstadoFisicoModel> entidades = repository.findAll();

        List<RpbiEstadoFisicoObject> dtos = new ArrayList<>();

        for (RpbiEstadoFisicoModel entidad : entidades){

            RpbiEstadoFisicoObject dto = new RpbiEstadoFisicoObject();

            dto.setId(entidad.getId());
            dto.setNombre(entidad.getNombre());
            dto.setUnidadMedida(entidad.getUnidadMedida());

            if(entidad.getEstatus() != null ){

                dto.setEstatusId(entidad.getEstatus().getId());
                dto.setEstatusNombre(entidad.getEstatus().getStatusName());
            }
            dtos.add(dto);
        }

        return dtos;
    }

}
