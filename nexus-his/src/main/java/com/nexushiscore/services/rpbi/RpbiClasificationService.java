package com.nexushiscore.services.rpbi;

import com.nexushiscore.beans.rpbi.RpbiClasificationObject;
import com.nexushiscore.models.rpbi.RpbiClasificationModel;
import com.nexushiscore.repositories.rpbi.IRpbiClasificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RpbiClasificationService {

    private final IRpbiClasificationRepository repository;

    // Método para obtener todo el catálogo convertido a DTO
    public List<RpbiClasificationObject> getAllClasificaciones() {
        // buscamos la data en crudo de la BD
        List<RpbiClasificationModel> entidades = repository.findAll();

        // preparamos la lista de respuesta
        List<RpbiClasificationObject> dtos = new ArrayList<>();

        // ciclo de mapeo
        for (RpbiClasificationModel entidad : entidades) {
            RpbiClasificationObject dto = new RpbiClasificationObject();

            // mapeo 1 a 1
            dto.setUuid(entidad.getUuid().toString());
            dto.setName(entidad.getName());
            dto.setDescription(entidad.getDescription());
            dto.setColorCode(entidad.getColorCode());

            // mapeo relacional
            if (entidad.getStatus() != null) {
                dto.setStatusUuid(entidad.getStatus().getUuid().toString());
                dto.setEstatusName(entidad.getStatus().getStatusName());
            }

            dtos.add(dto);
        }

        return dtos;
    }
}