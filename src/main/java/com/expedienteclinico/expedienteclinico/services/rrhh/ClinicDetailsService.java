package com.expedienteclinico.expedienteclinico.services.rrhh;

import com.expedienteclinico.expedienteclinico.beans.rrhh.ClinicDetailsObject;
import com.expedienteclinico.expedienteclinico.beans.rrhh.DepartmentObject;
import com.expedienteclinico.expedienteclinico.models.rrhh.ClinicDetailsModel;
import com.expedienteclinico.expedienteclinico.models.rrhh.DepartmentsModel;
import com.expedienteclinico.expedienteclinico.models.rrhh.EmployeesModel;
import com.expedienteclinico.expedienteclinico.models.system.StatusModel;
import com.expedienteclinico.expedienteclinico.repositories.rrhh.IClinicDetailsRepository;
import com.expedienteclinico.expedienteclinico.repositories.rrhh.IEmployeesRepository;
import com.expedienteclinico.expedienteclinico.repositories.system.IStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClinicDetailsService {
    @Autowired
    IClinicDetailsRepository clinicDetailsRepository;

    @Autowired
    IStatusRepository statusRepo;

    IEmployeesRepository employeesRepository;

    //Filtro por nombre del estado
    private StatusModel getStatusByName(String statusName) {
        return statusRepo.findByStatusNameIgnoreCase(statusName)
                .orElseThrow(() -> new RuntimeException("Error: El estado '" + statusName + "' no existe en la base de datos."));
    }

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

        if (model.getId_status() != null) {
            dto.setStatusName(model.getId_status().getStatusName());
        }

        return dto;
    }

    public List<ClinicDetailsObject> getAll() {
        return clinicDetailsRepository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }

//    public ClinicDetailsObject saveInfo(ClinicDetailsObject dto) {
//        if (clinicDetailsRepository.existsByProfessionalLicense(dto.getProfessionalLicense())) {
//            throw new RuntimeException("Error: Ya existe un registro con la cédula '" + dto.getProfessionalLicense() + "'.");
//        }
//
//        EmployeesModel employee = employeesRepository.findById(dto.getEmployeeId())
//                .orElseThrow(() -> new RuntimeException("Error: El empleado no existe."));
//
//        StatusModel status = getStatusByName("Active");
//        ClinicDetailsModel model = new ClinicDetailsModel();
//        model.setEmployee(employee);
//        model.setProfessionalLicense(dto.getProfessionalLicense());
//        model.setGraduationInstitution(dto.getGraduationInstitution());
//        model.setSpecialty(dto.getSpecialty());
//        model.setId_status(status);
//
//        ClinicDetailsModel saved = clinicDetailsRepository.save(model);
//        return convertToDTO(saved);
//    }
//
//    public ClinicDetailsObject updateInfo(Long id, ClinicDetailsObject dto) {
//        return clinicDetailsRepository.findById(id).map(model -> {
//            // Validar cédula si ha cambiado
//            if (!model.getProfessionalLicense().equalsIgnoreCase(dto.getProfessionalLicense())) {
//                if (clinicDetailsRepository.existsByProfessionalLicense(dto.getProfessionalLicense())) {
//                    throw new RuntimeException("Error: La nueva cédula ya está registrada.");
//                }
//                model.setProfessionalLicense(dto.getProfessionalLicense());
//            }
//
//            model.setGraduationInstitution(dto.getGraduationInstitution());
//            model.setSpecialty(dto.getSpecialty());
//
//            ClinicDetailsModel updated = clinicDetailsRepository.save(model);
//            return convertToDTO(updated);
//        }).orElseThrow(() -> new RuntimeException("Error: Detalle clínico no encontrado."));
//    }
//
//    public boolean deleteInfo(Long id) {
//        return clinicDetailsRepository.findById(id).map(model -> {
//            StatusModel inactive = getStatusByName("Inactive");
//            model.setId_status(inactive);
//            clinicDetailsRepository.save(model);
//            return true;
//        }).orElse(false);
//    }

}
