package com.expedienteclinico.expedienteclinico.services.rrhh;

import com.expedienteclinico.expedienteclinico.beans.rrhh.ContractObject;
import com.expedienteclinico.expedienteclinico.models.rrhh.ContractsModel;
import com.expedienteclinico.expedienteclinico.repositories.rrhh.IContractsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContractsService {
    @Autowired
    IContractsRepository contractsRepository;
    public ContractObject convertToDTO(ContractsModel model) {
        ContractObject dto = new ContractObject();
        dto.setId(model.getId());
        dto.setUuid(model.getUuid());

        // Mapeo del empleado
        if (model.getEmployee() != null) {
            dto.setEmployeeId(model.getEmployee().getId());
            dto.setEmployeeName(model.getEmployee().getName());
        }

        dto.setContractType(model.getContractType());
        dto.setHiringDate(model.getHiringDate());
        dto.setTerminationDate(model.getTerminationDate());
        dto.setBaseSalary(model.getBaseSalary());

        // Mapeo del estatus
        if (model.getStatus() != null) {
            dto.setStatusName(model.getStatus().getStatusName());
        }

        return dto;
    }

    public List<ContractObject> getAll() {
        return contractsRepository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }


}
