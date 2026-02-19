package com.expedienteclinico.expedienteclinico.services.rpbi;

import com.expedienteclinico.expedienteclinico.models.rpbi.RpbiContainerModel;
import com.expedienteclinico.expedienteclinico.repositories.rpbi.IRpbiContainerRepository;
import com.expedienteclinico.expedienteclinico.beans.rpbi.RpbiContainerObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RpbiContainerService {

@Autowired
    private IRpbiContainerRepository repository;

public List<RpbiContainerObject> getAll(){

    List<RpbiContainerModel> entidades = repository.findAll();
    List<RpbiContainerObject> dtos = new ArrayList<>();

    for (RpbiContainerModel entidad : entidades){

        RpbiContainerObject dto = new RpbiContainerObject();

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
