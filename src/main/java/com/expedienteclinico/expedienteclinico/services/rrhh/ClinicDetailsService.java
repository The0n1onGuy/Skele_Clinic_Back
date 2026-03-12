package com.expedienteclinico.expedienteclinico.services.rrhh;

import com.expedienteclinico.expedienteclinico.beans.rrhh.ClinicDetailsObject;
import com.expedienteclinico.expedienteclinico.models.rrhh.ClinicDetailsModel;
import com.expedienteclinico.expedienteclinico.repositories.rrhh.IClinicDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClinicDetailsService {
    @Autowired
    IClinicDetailsRepository clinicDetailsRepository;

    public ClinicDetailsObject convertToDTO(ClinicDetailsModel model) {
        ClinicDetailsObject dto = new ClinicDetailsObject();
        dto.setId(model.getId());
        dto.setUuid(model.getUuid());

        if (model.getEmployee() != null) {
            dto.setEmployeeId(model.getEmployee().getId());
            dto.setEmployeeFullName(String.format("%s %s %s",
                    model.getEmployee().getName(),
                    model.getEmployee().getPatname(),
                    model.getEmployee().getMatname()));
        }

        dto.setProfessionalLicense(model.getProfessionalLicense());
        dto.setGraduationInstitution(model.getGraduationInstitution());
        dto.setSpecialty(model.getSpecialty());

        if (model.getStatus() != null) {
            dto.setStatusName(model.getStatus().getStatusName());
        }

        return dto;
    }

    public List<ClinicDetailsObject> getAll() {
        return clinicDetailsRepository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }

}
