package com.expedienteclinico.expedienteclinico.services.rrhh;

import com.expedienteclinico.expedienteclinico.beans.rrhh.EmployeesObject;
import com.expedienteclinico.expedienteclinico.models.rrhh.EmployeesModel;
import com.expedienteclinico.expedienteclinico.repositories.rrhh.IEmployeesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeesService {
    @Autowired
    IEmployeesRepository employeesRepository;
    public EmployeesObject convertToDTO(EmployeesModel model) {
        EmployeesObject dto = new EmployeesObject();
        dto.setId(model.getId());
        dto.setUuid(model.getUuid());
        dto.setName(model.getName());
        dto.setPatname(model.getPatname());
        dto.setMatname(model.getMatname());

        dto.setFullName(String.format("%s %s %s", model.getName(), model.getPatname(), model.getMatname()));

        dto.setCurp(model.getCurp());
        dto.setRfc(model.getRfc());
        dto.setDatebirth(model.getDatebirth());
        dto.setDatereg(model.getDatereg());
        dto.setGender(model.getGender());

        if (model.getId_position() != null) {
            dto.setPositionId(model.getId_position().getId());
            dto.setPositionName(model.getId_position().getName());
        }

        if (model.getId_department() != null) {
            dto.setDepartmentId(model.getId_department().getId());
            dto.setDepartmentName(model.getId_department().getName());
        }

        if (model.getStatus() != null) {
            dto.setStatusName(model.getStatus().getStatusName());
        }

        return dto;
    }
    public List<EmployeesObject> getAll() {
        return employeesRepository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }


}
