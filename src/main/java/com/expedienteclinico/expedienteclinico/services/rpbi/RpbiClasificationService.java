package com.expedienteclinico.expedienteclinico.services.rpbi;

import com.expedienteclinico.expedienteclinico.beans.rpbi.RpbiClasificationObject; // Asegúrate de crear este DTO como vimos antes
import com.expedienteclinico.expedienteclinico.models.rpbi.RpbiClasificationModel;
import com.expedienteclinico.expedienteclinico.repositories.rpbi.IRpbiClasificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RpbiClasificationService {

    @Autowired
    private IRpbiClasificationRepository repository;

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
            dto.setId(entidad.getId());
            dto.setNombre(entidad.getNombre());
            dto.setDescripcion(entidad.getDescripcion());
            dto.setCodigoColor(entidad.getCodigoColor());

            // mapeo relacional
            if (entidad.getEstatus() != null) {
                // se separa el id y nombre, segun los campos declarados en el Dto
                dto.setEstatusId(entidad.getEstatus().getId());
                dto.setEstatusNombre(entidad.getEstatus().getStatusName());
            }

            dtos.add(dto);
        }

        return dtos;
    }
}