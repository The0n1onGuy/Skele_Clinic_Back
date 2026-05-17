package com.nexushiscore.services.rrhh;

import com.nexushiscore.beans.rrhh.EmployeeRequestObject;
import com.nexushiscore.beans.rrhh.EmployeesObject;
import com.nexushiscore.models.rrhh.DepartmentsModel;
import com.nexushiscore.models.rrhh.EmployeesModel;
import com.nexushiscore.models.rrhh.PositionsModel;
import com.nexushiscore.models.system.StatusModel;
import com.nexushiscore.repositories.rrhh.IDepartmentsRepository;
import com.nexushiscore.repositories.rrhh.IEmployeesRepository;
import com.nexushiscore.repositories.rrhh.IPositionsRepository;
import com.nexushiscore.repositories.system.IStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeesService {
    @Autowired IEmployeesRepository employeesRepository;

    @Autowired IDepartmentsRepository departmentsRepository;

    @Autowired IPositionsRepository positionsRepository;

    @Autowired IStatusRepository statusRepo;

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

    public EmployeesObject createEmployeeRequest(EmployeeRequestObject request) {
        // 1. Uniqueness check (Security layer)
        if (employeesRepository.existsBycurp(request.getCurp())) {
            throw new RuntimeException("Security Error: An employee with CURP " + request.getCurp() + " already exists.");
        }

        // 2. Internal Verification: Convert Text to Objects (Lowercase matching)
        DepartmentsModel dept = departmentsRepository.findByNameIgnoreCase(request.getDepartmentName().toLowerCase())
                .orElseThrow(() -> new RuntimeException("Verification Failed: Department '" + request.getDepartmentName() + "' not found."));

        PositionsModel pos = positionsRepository.findByNameIgnoreCase(request.getPositionName().toLowerCase())
                .orElseThrow(() -> new RuntimeException("Verification Failed: Position '" + request.getPositionName() + "' not found."));

        StatusModel activeStatus = statusRepo.findByStatusNameIgnoreCase("Active")
                .orElseThrow(() -> new RuntimeException("System Error: Active status not configured."));

        // 3. Map Request to the Entity Model
        EmployeesModel newModel = new EmployeesModel();
        newModel.setName(request.getName());
        newModel.setPatname(request.getPatname());
        newModel.setMatname(request.getMatname());
        newModel.setCurp(request.getCurp());
        newModel.setRfc(request.getRfc());
        newModel.setDatebirth(request.getDatebirth());
        newModel.setDatereg(request.getDatereg());
        newModel.setGender(request.getGender());

        newModel.setId_department(dept);
        newModel.setId_position(pos);
        newModel.setId_status(activeStatus);

        // 4. Proceed to actual population
        return createEmployee(newModel);
    }

    /**
     * Phase 2: CreateEmployee
     * This method ACTUALLY populates the table once the info is verified.
     */
    public EmployeesObject createEmployee(EmployeesModel model) {
        try {
            EmployeesModel saved = employeesRepository.save(model);
            return convertToDTO(saved);
        } catch (Exception e) {
            throw new RuntimeException("Database Error: Could not populate employee table. " + e.getMessage());
        }
    }

//    public EmployeesObject saveInfo(EmployeesObject dto) {
//        if (employeesRepository.existsByNameIgnoreCase(dto.getName())) {
//            throw new RuntimeException("Error: Ya existe este empleado '" + dto.getName() + "'.");
//        }
//
//        EmployeesModel model = new EmployeesModel();
//        //Nombre de la persona
//        model.setName(dto.getName());
//        model.setPatname(dto.getPatname());
//        model.setMatname(dto.getMatname());
//
//        //Detalles personales
//        model.setCurp(dto.getCurp());
//        model.setRfc(dto.getRfc());
//        model.setDatebirth(dto.getDatebirth());
//        model.setDatereg(dto.getDatereg());
//
//        DepartmentsModel dept = departmentsRepository.findById(dto.getDepartmentId())
//                .orElseThrow(() -> new RuntimeException("Error: El departamento seleccionado no existe."));
//        PositionsModel pos = positionsRepository.findById(dto.getPositionId())
//                .orElseThrow(() -> new RuntimeException("Error: El puesto/posición seleccionada no existe."));
//        StatusModel status = statusRepo.findByStatusNameIgnoreCase("Active")
//                .orElseThrow(() -> new RuntimeException("Estado no encontrado"));
//        model.setId_department(dept);
//
//        model.setId_position(pos);
//        model.setId_status(status);
//
//        EmployeesModel saved = employeesRepository.save(model);
//        return convertToDTO(saved);
//    }

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
