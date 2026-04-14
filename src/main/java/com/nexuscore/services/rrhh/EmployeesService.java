package com.nexuscore.services.rrhh;

import com.nexuscore.beans.rrhh.EmployeesObject;
import com.nexuscore.models.rrhh.DepartmentsModel;
import com.nexuscore.models.rrhh.EmployeesModel;
import com.nexuscore.models.rrhh.PositionsModel;
import com.nexuscore.models.system.StatusModel;
import com.nexuscore.repositories.rrhh.IDepartmentsRepository;
import com.nexuscore.repositories.rrhh.IEmployeesRepository;
import com.nexuscore.repositories.rrhh.IPositionsRepository;
import com.nexuscore.repositories.system.IStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeesService {
    @Autowired
    IEmployeesRepository employeesRepository;

    @Autowired
    IDepartmentsRepository departmentsRepository;

    @Autowired
    IPositionsRepository positionsRepository;

    @Autowired
    IStatusRepository statusRepo;

    //Filtro por nombre del estado
    private StatusModel getStatusByName(String statusName) {
        return statusRepo.findByStatusNameIgnoreCase(statusName)
                .orElseThrow(() -> new RuntimeException("Error: El estado '" + statusName + "' no existe en la base de datos."));
    }

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

        if (model.getId_status() != null) {
            dto.setStatusName(model.getId_status().getStatusName());
        }

        return dto;
    }
    public List<EmployeesObject> getAll() {
        return employeesRepository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }

    public EmployeesObject saveInfo(EmployeesObject dto) {
        if (employeesRepository.existsByNameIgnoreCase(dto.getName())) {
            throw new RuntimeException("Error: Ya existe este empleado '" + dto.getName() + "'.");
        }

        EmployeesModel model = new EmployeesModel();
        //Nombre de la persona, atomizado
        model.setName(dto.getName());
        model.setPatname(dto.getPatname());
        model.setMatname(dto.getMatname());

        //Detalles personales
        model.setCurp(dto.getCurp());
        model.setRfc(dto.getRfc());
        model.setDatebirth(dto.getDatebirth());
        model.setDatereg(dto.getDatereg());

        DepartmentsModel dept = departmentsRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Error: El departamento seleccionado no existe."));
        PositionsModel pos = positionsRepository.findById(dto.getPositionId())
                .orElseThrow(() -> new RuntimeException("Error: El puesto/posición seleccionada no existe."));
        StatusModel status = statusRepo.findByStatusNameIgnoreCase("Active")
                .orElseThrow(() -> new RuntimeException("Estado no encontrado"));
        model.setId_department(dept);
        model.setId_position(pos);
        model.setId_status(status);

        EmployeesModel saved = employeesRepository.save(model);
        return convertToDTO(saved);
    }

    public EmployeesObject updateInfo(Long id, EmployeesObject dto) {
        return employeesRepository.findById(id).map(model -> {
            if (!model.getName().equalsIgnoreCase(dto.getName())) {
                if (employeesRepository.existsByNameIgnoreCase(dto.getName())) {
                    throw new RuntimeException("Error: No se puede actualizar. El nombre '" + dto.getName() + "' ya está en uso por otro departamento.");
                }
                model.setName(dto.getName());
                model.setPatname(dto.getPatname());
                model.setMatname(dto.getMatname());
                //Detalles personales
                model.setCurp(dto.getCurp());
                model.setRfc(dto.getRfc());
                model.setDatebirth(dto.getDatebirth());
                model.setDatereg(dto.getDatereg());
                DepartmentsModel dept = departmentsRepository.findById(dto.getDepartmentId())
                        .orElseThrow(() -> new RuntimeException("Error: El departamento seleccionado no existe."));
                PositionsModel pos = positionsRepository.findById(dto.getPositionId())
                        .orElseThrow(() -> new RuntimeException("Error: El puesto/posición seleccionada no existe."));
                StatusModel status = statusRepo.findByStatusNameIgnoreCase("Active")
                        .orElseThrow(() -> new RuntimeException("Estado no encontrado"));
                model.setId_department(dept);
                model.setId_position(pos);
                model.setId_status(status);


            }
            EmployeesModel  updated = employeesRepository.save(model);
            return convertToDTO(updated);
        }).orElse(null);
    }

    public boolean deleteInfo(Long id) {
        return employeesRepository.findById(id).map(employee -> {
            StatusModel statusInactive = getStatusByName("Inactive");
            employee.setId_status(statusInactive);
            employeesRepository.save(employee);
            return true;
        }).orElse(false);
    }

}
