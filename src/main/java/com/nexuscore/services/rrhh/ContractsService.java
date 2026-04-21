package com.nexuscore.services.rrhh;

import com.nexuscore.beans.rrhh.ContractObject;
import com.nexuscore.models.rrhh.ContractsModel;
import com.nexuscore.models.rrhh.EmployeesModel;
import com.nexuscore.models.system.StatusModel;
import com.nexuscore.repositories.rrhh.IContractsRepository;
import com.nexuscore.repositories.rrhh.IEmployeesRepository;
import com.nexuscore.repositories.system.IStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContractsService {
    @Autowired
    IContractsRepository contractsRepository;

    IEmployeesRepository employeesRepository;

    @Autowired
    IStatusRepository statusRepo;

    //Filtro por nombre del estado
    private StatusModel getStatusByName(String statusName) {
        return statusRepo.findByStatusNameIgnoreCase(statusName)
                .orElseThrow(() -> new RuntimeException("Error: El estado '" + statusName + "' no existe en la base de datos."));
    }

    public ContractObject convertToDTO(ContractsModel model) {
        ContractObject dto = new ContractObject();
        dto.setId(model.getId());
        dto.setUuid(model.getUuid().toString().toString());

        // Mapeo del empleado
        if (model.getId_employee() != null) {
            dto.setEmployeeId(model.getId_employee().getId());
            dto.setEmployeeName(model.getId_employee().getName());
        }

        dto.setContractType(model.getContractType());
        dto.setHiringDate(model.getHiringDate());
        dto.setTerminationDate(model.getTerminationDate());
        dto.setBaseSalary(model.getBaseSalary());

        // Mapeo del estatus
        if (model.getId_status() != null) {
            dto.setStatusName(model.getId_status().getStatusName());
        }

        return dto;
    }

    public List<ContractObject> getAll() {
        return contractsRepository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }

    public ContractObject saveInfo(ContractObject dto) {
        EmployeesModel employee = employeesRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Error: El empleado con ID " + dto.getEmployeeId() + " no existe."));

        StatusModel activeStatus = getStatusByName("Active");

        ContractsModel model = new ContractsModel();
        model.setId_employee(employee);
        model.setContractType(dto.getContractType());
        model.setHiringDate(dto.getHiringDate());
        model.setTerminationDate(dto.getTerminationDate());
        model.setBaseSalary(dto.getBaseSalary());
        model.setId_status(activeStatus);

        ContractsModel saved = contractsRepository.save(model);
        return convertToDTO(saved);
    }

    public ContractObject updateInfo(Long id, ContractObject dto) {
        return contractsRepository.findById(id).map(model -> {
            if (!model.getId_employee().getId().equals(dto.getEmployeeId())) {
                EmployeesModel newEmployee = employeesRepository.findById(dto.getEmployeeId())
                        .orElseThrow(() -> new RuntimeException("Error: El nuevo empleado no existe."));
                model.setId_employee(newEmployee);
            }

            model.setContractType(dto.getContractType());
            model.setHiringDate(dto.getHiringDate());
            model.setTerminationDate(dto.getTerminationDate());
            model.setBaseSalary(dto.getBaseSalary());

            ContractsModel updated = contractsRepository.save(model);
            return convertToDTO(updated);
        }).orElseThrow(() -> new RuntimeException("Error: Contrato no encontrado con ID: " + id));
    }

    public boolean deleteInfo(Long id) {
        return contractsRepository.findById(id).map(contract -> {
            StatusModel inactiveStatus = getStatusByName("Inactive");
            contract.setId_status(inactiveStatus);
            contractsRepository.save(contract);
            return true;
        }).orElse(false);
    }
    
}
