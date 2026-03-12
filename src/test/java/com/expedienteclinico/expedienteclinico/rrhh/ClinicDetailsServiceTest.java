package com.expedienteclinico.expedienteclinico.rrhh;

import com.expedienteclinico.expedienteclinico.beans.rrhh.ClinicDetailsObject;
import com.expedienteclinico.expedienteclinico.models.rrhh.ClinicDetailsModel;
import com.expedienteclinico.expedienteclinico.models.rrhh.EmployeesModel;
import com.expedienteclinico.expedienteclinico.models.system.StatusModel;
import com.expedienteclinico.expedienteclinico.repositories.rrhh.IClinicDetailsRepository;
import com.expedienteclinico.expedienteclinico.services.rrhh.ClinicDetailsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClinicDetailsServiceTest {

    @Mock
    private IClinicDetailsRepository clinicDetailsRepository;

    @InjectMocks
    private ClinicDetailsService clinicDetailsService;

    @Test
    @DisplayName("GET: Conversión exitosa de Detalles Clínicos a DTO")
    void testConvertToDTOSuccess() {
        ClinicDetailsModel model = new ClinicDetailsModel();
        model.setProfessionalLicense("12345678");
        model.setSpecialty("Cardiología");

        EmployeesModel emp = new EmployeesModel();
        emp.setName("Olan");
        emp.setPatname("M.");
        emp.setMatname("S.");
        model.setEmployee(emp);

        StatusModel status = new StatusModel();
        status.setStatusName("Active");
        model.setStatus(status);

        ClinicDetailsObject dto = clinicDetailsService.convertToDTO(model);

        assertNotNull(dto);
        assertEquals("Cardiología", dto.getSpecialty());
        assertTrue(dto.getEmployeeFullName().contains("Olan"));
        assertEquals("Active", dto.getStatusName());
    }

    @Test
    @DisplayName("GET: Manejo de valores nulos en relaciones (Inconsistencia de BD)")
    void testConvertToDTONullRelations() {
        ClinicDetailsModel model = new ClinicDetailsModel();
        model.setProfessionalLicense("999999");
        model.setEmployee(null);
        model.setStatus(null);

        ClinicDetailsObject dto = clinicDetailsService.convertToDTO(model);

        assertNotNull(dto);
        assertNull(dto.getEmployeeFullName());
        assertNull(dto.getStatusName());
        assertEquals("999999", dto.getProfessionalLicense());
    }
}