package com.nexushiscore.services.rrhh;

import com.nexushiscore.beans.rrhh.PositionsObject;
import com.nexushiscore.models.rrhh.PositionsModel;
import com.nexushiscore.models.system.StatusModel;
import com.nexushiscore.repositories.rrhh.IPositionsRepository;
import com.nexushiscore.repositories.system.IStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PositionsService {
    @Autowired
    IPositionsRepository positionsRepo;

    @Autowired
    IStatusRepository statusRepo;

    @Value("${STATUS1:Active}") private String Active;
    @Value("${STATUS2:Inactive}") private String Inactive;

    //Filtro por nombre del estado
    private StatusModel getStatusByName(String statusName) {
        return statusRepo.findByStatusNameIgnoreCase(statusName)
                .orElseThrow(() -> new RuntimeException("Error: El estado '" + statusName + "' no existe en la base de datos."));
    }

    public PositionsObject convertToDTO(PositionsModel model) {
        PositionsObject dto = new PositionsObject();
        dto.setId(model.getId());
        dto.setUuid(model.getUuid());
        dto.setName(model.getName());
        dto.setDescription(model.getDescription());

        // Mapeo seguro del estatus
        if (model.getId_status() != null) {
            dto.setStatusName(model.getId_status().getStatusName());
        }

        return dto;
    }

    public List<PositionsObject> getAll() {
        return positionsRepo.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }

    public PositionsObject saveInfo(PositionsObject dto) {
        if (positionsRepo.existsByNameIgnoreCase(dto.getName())) {
            throw new RuntimeException("Error: Ya existe un departamento con el nombre '" + dto.getName() + "'.");
        }
        PositionsModel model = new PositionsModel();
        model.setName(dto.getName());

        StatusModel status = statusRepo.findByStatusNameIgnoreCase("Active")
                .orElseThrow(() -> new RuntimeException("Estado no encontrado"));
        model.setId_status(status);

        PositionsModel saved = positionsRepo.save(model);
        return convertToDTO(saved);
    }

    public PositionsObject updateInfo(Long id, PositionsObject dto) {
        return positionsRepo.findById(id).map(model -> {
            if (!model.getName().equalsIgnoreCase(dto.getName())) {
                if (positionsRepo.existsByNameIgnoreCase(dto.getName())) {
                    throw new RuntimeException("Error: No se puede actualizar. La posicion '" + dto.getName() + "' ya existe .");
                }
                model.setName(dto.getName());
            }
            PositionsModel updated = positionsRepo.save(model);
            return convertToDTO(updated);
        }).orElse(null);
    }

    public boolean deleteInfo(Long id) {
        return positionsRepo.findById(id).map(depto -> {
            StatusModel statusInactive = getStatusByName(Inactive);
            depto.setId_status(statusInactive);
            positionsRepo.save(depto);
            return true;
        }).orElse(false);
    }

}
