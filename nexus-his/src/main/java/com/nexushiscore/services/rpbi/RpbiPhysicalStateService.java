package com.nexushiscore.services.rpbi;

import com.nexushiscore.beans.rpbi.RpbiPhysicalStateObject;
import com.nexushiscore.models.rpbi.RpbiPhysicalStateModel;
import com.nexushiscore.repositories.rpbi.IRpbiPhysicalStateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RpbiPhysicalStateService {

    private final IRpbiPhysicalStateRepository repository;

    public List<RpbiPhysicalStateObject> getAll(){

        List<RpbiPhysicalStateModel> entidades = repository.findAll();

        List<RpbiPhysicalStateObject> dtos = new ArrayList<>();

        for (RpbiPhysicalStateModel entidad : entidades){

            RpbiPhysicalStateObject dto = new RpbiPhysicalStateObject();

            dto.setUuid(entidad.getUuid().toString());
            dto.setName(entidad.getName());
            dto.setMeasureUnit(entidad.getMeasureUnit());

            if(entidad.getStatus() != null ){

                dto.setStatusUuid(entidad.getStatus().getUuid().toString());
                dto.setEstatusName(entidad.getStatus().getStatusName());
            }
            dtos.add(dto);
        }

        return dtos;
    }

}
