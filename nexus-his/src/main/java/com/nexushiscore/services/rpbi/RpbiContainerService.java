package com.nexushiscore.services.rpbi;

import com.nexushiscore.beans.rpbi.RpbiContainerObject;
import com.nexushiscore.models.rpbi.RpbiContainerModel;
import com.nexushiscore.repositories.rpbi.IRpbiContainerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RpbiContainerService {

    private final IRpbiContainerRepository repository;

public List<RpbiContainerObject> getAll(){

    List<RpbiContainerModel> entidades = repository.findAll();
    List<RpbiContainerObject> dtos = new ArrayList<>();

    for (RpbiContainerModel entidad : entidades){

        RpbiContainerObject dto = new RpbiContainerObject();

        dto.setUuid(entidad.getUuid().toString());
        dto.setName(entidad.getName());
        dto.setDescription(entidad.getDescription());
        if(entidad.getStatus() != null ){

            dto.setStatusUuid(entidad.getStatus().getUuid().toString());
            dto.setEstatusName(entidad.getStatus().getStatusName());
        }
        dtos.add(dto);
    }
return dtos;
}

}
