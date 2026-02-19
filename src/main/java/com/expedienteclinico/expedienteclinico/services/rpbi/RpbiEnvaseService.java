package com.expedienteclinico.expedienteclinico.services.rpbi;

import com.expedienteclinico.expedienteclinico.beans.rpbi.RpbiClasificacionObject;
import com.expedienteclinico.expedienteclinico.models.rpbi.RpbiEnvaseModel;
import com.expedienteclinico.expedienteclinico.repositories.rpbi.IRpbiEnvaseRepository;
import com.expedienteclinico.expedienteclinico.beans.rpbi.RpbiEnvaseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RpbiEnvaseService {

@Autowired
    private IRpbiEnvaseRepository repository;

public List<RpbiEnvaseObject> getAll(){

    List<RpbiEnvaseModel> entidades = repository.findAll();
    List<RpbiEnvaseObject> dtos = new ArrayList<>();

    for (RpbiEnvaseModel entidad : entidades){

        RpbiEnvaseObject dto = new RpbiEnvaseObject();

        dto.setId(entidad.getId());
        dto.setNombre(entidad.getNombre());
        dto.setDescripcion(entidad.getDescripcion());
        if(entidad.getEstatus() != null ){

            dto.setEstatusId(entidad.getEstatus().getId());
            dto.setEstatusNombre(entidad.getEstatus().getStatusName());
        }
        dtos.add(dto);
    }
return dtos;
}

}
