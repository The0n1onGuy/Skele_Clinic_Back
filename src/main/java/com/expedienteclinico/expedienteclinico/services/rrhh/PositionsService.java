package com.expedienteclinico.expedienteclinico.services.rrhh;
import com.expedienteclinico.expedienteclinico.beans.rrhh.PositionsObject;
import com.expedienteclinico.expedienteclinico.models.rrhh.PositionsModel;
import com.expedienteclinico.expedienteclinico.repositories.rrhh.IPositionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PositionsService {
    @Autowired
    IPositionsRepository positionsRepo;

    public PositionsObject convertToDTO(PositionsModel model) {
        PositionsObject dto = new PositionsObject();
        dto.setId(model.getId());
        dto.setUuid(model.getUuid());
        dto.setName(model.getName());
        dto.setDescription(model.getDescription());

        // Mapeo seguro del estatus
        if (model.getStatus() != null) {
            dto.setStatusName(model.getStatus().getStatusName());
        }

        return dto;
    }

    public List<PositionsObject> getAll() {
        return positionsRepo.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }

}
