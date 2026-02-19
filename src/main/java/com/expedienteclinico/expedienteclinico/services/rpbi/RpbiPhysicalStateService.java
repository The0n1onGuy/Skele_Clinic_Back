package com.expedienteclinico.expedienteclinico.services.rpbi;

import com.expedienteclinico.expedienteclinico.beans.rpbi.RpbiPhysicalStateObject;
import com.expedienteclinico.expedienteclinico.repositories.rpbi.IRpbiPhysicalStateRepository;
import com.expedienteclinico.expedienteclinico.models.rpbi.RpbiPhysicalStateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RpbiPhysicalStateService {

    @Autowired
    private IRpbiPhysicalStateRepository repository;

    public List<RpbiPhysicalStateObject> getAll(){

        List<RpbiPhysicalStateModel> entidades = repository.findAll();

        List<RpbiPhysicalStateObject> dtos = new ArrayList<>();

        for (RpbiPhysicalStateModel entidad : entidades){

            RpbiPhysicalStateObject dto = new RpbiPhysicalStateObject();

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
