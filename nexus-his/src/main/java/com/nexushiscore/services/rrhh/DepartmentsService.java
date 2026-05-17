package com.nexushiscore.services.rrhh;

import com.nexushiscore.beans.rrhh.DepartmentObject;
import com.nexushiscore.models.rrhh.DepartmentsModel;
import com.nexushiscore.models.system.StatusModel;
import com.nexushiscore.repositories.rrhh.IDepartmentsRepository;
import com.nexushiscore.repositories.system.IStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class DepartmentsService {
    @Autowired
    IDepartmentsRepository departmentsRepo;

    @Autowired
    IStatusRepository statusRepo;


    @Value("${STATUS1:Active}") private String Active;
    @Value("${STATUS2:Inactive}") private String Inactive;

    //Filtro por nombre del estado
    private StatusModel getStatusByName(String statusName) {
        return statusRepo.findByStatusNameIgnoreCase(statusName)
                .orElseThrow(() -> new RuntimeException("Error: El estado '" + statusName + "' no existe en la base de datos."));
    }
    //Conversor al Bean
    public DepartmentObject convertToDTO(DepartmentsModel model) {
        DepartmentObject dto = new DepartmentObject();
        dto.setId(model.getId());
        dto.setName(model.getName());
        dto.setUuid(model.getUuid());
        dto.setStatusName(model.getId_status().getStatusName());
        return dto;
    }

    public List<DepartmentObject> getAll() {
        return departmentsRepo.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }

    public DepartmentObject saveInfo(DepartmentObject dto) {
        if (departmentsRepo.existsByNameIgnoreCase(dto.getName())) {
            throw new RuntimeException("Error: Ya existe un departamento con el nombre '" + dto.getName() + "'.");
        }
        DepartmentsModel model = new DepartmentsModel();
        model.setName(dto.getName());

        StatusModel status = statusRepo.findByStatusNameIgnoreCase("Active")
                .orElseThrow(() -> new RuntimeException("Estado no encontrado"));
        model.setId_status(status);

        DepartmentsModel saved = departmentsRepo.save(model);
        return convertToDTO(saved);
    }

    public DepartmentObject updateInfo(Long id, DepartmentObject dto) {
        return departmentsRepo.findById(id).map(model -> {
            if (!model.getName().equalsIgnoreCase(dto.getName())) {
                if (departmentsRepo.existsByNameIgnoreCase(dto.getName())) {
                    throw new RuntimeException("Error: No se puede actualizar. El nombre '" + dto.getName() + "' ya está en uso por otro departamento.");
                }
                model.setName(dto.getName());
            }
            DepartmentsModel updated = departmentsRepo.save(model);
            return convertToDTO(updated);
        }).orElse(null);
    }

    public boolean deleteInfo(Long id) {
        return departmentsRepo.findById(id).map(depto -> {
            StatusModel statusInactive = getStatusByName(Inactive);
            depto.setId_status(statusInactive);
            departmentsRepo.save(depto);
            return true;
        }).orElse(false);
    }
}
