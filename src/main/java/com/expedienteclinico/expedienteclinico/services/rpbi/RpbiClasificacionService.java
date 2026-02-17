package com.expedienteclinico.expedienteclinico.services.rpbi;

import com.expedienteclinico.expedienteclinico.beans.rpbi.RpbiClasificacionObject; // Asegúrate de crear este DTO como vimos antes
import com.expedienteclinico.expedienteclinico.models.rpbi.RpbiClasificacionModel;
import com.expedienteclinico.expedienteclinico.repositories.rpbi.IRpbiClasificacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RpbiClasificacionService {

    @Autowired
    private IRpbiClasificacionRepository repository;

    // Método para obtener todo el catálogo convertido a DTO
    public List<RpbiClasificacionObject> getAllClasificaciones() {
        // buscamos la data en crudo de la BD
        List<RpbiClasificacionModel> entidades = repository.findAll();

        // preparamos la lista de respuesta
        List<RpbiClasificacionObject> dtos = new ArrayList<>();

        // ciclo de mapeo
        for (RpbiClasificacionModel entidad : entidades) {
            RpbiClasificacionObject dto = new RpbiClasificacionObject();

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