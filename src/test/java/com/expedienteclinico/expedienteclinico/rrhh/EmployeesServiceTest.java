package com.expedienteclinico.expedienteclinico.rrhh;
import com.expedienteclinico.expedienteclinico.beans.rrhh.EmployeesObject;
import com.expedienteclinico.expedienteclinico.models.rrhh.DepartmentsModel;
import com.expedienteclinico.expedienteclinico.models.rrhh.EmployeesModel;
import com.expedienteclinico.expedienteclinico.models.system.StatusModel;
import com.expedienteclinico.expedienteclinico.repositories.rrhh.IDepartmentsRepository;
import com.expedienteclinico.expedienteclinico.repositories.rrhh.IEmployeesRepository;
import com.expedienteclinico.expedienteclinico.repositories.rrhh.IPositionsRepository;
import com.expedienteclinico.expedienteclinico.repositories.system.IStatusRepository;
import com.expedienteclinico.expedienteclinico.services.rrhh.EmployeesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeesServiceTest {

    @Mock private IEmployeesRepository employeeRepo;
    @Mock private IDepartmentsRepository deptRepo;
    @Mock private IPositionsRepository posRepo;
    @Mock private IStatusRepository statusRepo;

    @InjectMocks private EmployeesService employeesService;
    private StatusModel activeStatus;
    private EmployeesModel employeesModel;
    @BeforeEach
    void setUp() {
        activeStatus = new StatusModel();
        activeStatus.setId(1L);
        activeStatus.setStatusName("Active");

        employeesModel = new EmployeesModel();
        employeesModel.setId(1L);
        employeesModel.setName("Sistemas");
        employeesModel.setUuid(UUID.randomUUID());
        employeesModel.setId_status(activeStatus);
        ReflectionTestUtils.setField(employeesService, "Active", "Active");
        ReflectionTestUtils.setField(employeesService, "Inactive", "Inactive");
    }

    @Test
    @DisplayName("GET: Conversión exitosa con todas las llaves foráneas")
    void testConvertToDTOSuccess() {
        EmployeesModel model = new EmployeesModel();
        model.setName("Olan");
        model.setUuid(UUID.randomUUID());

        DepartmentsModel dept = new DepartmentsModel();
        dept.setName("Sistemas");
        model.setId_department(dept);

        EmployeesObject dto = employeesService.convertToDTO(model);

        assertEquals("Olan", dto.getName());
        assertEquals("Sistemas", dto.getDepartmentName());
    }

    @Test
    @DisplayName("GET: Manejo de error cuando el Departamento es NULL")
    void testConvertToDTONullDepartment() {
        EmployeesModel model = new EmployeesModel();
        model.setName("Olan");
        model.setId_department(null);

        EmployeesObject dto = employeesService.convertToDTO(model);

        assertNotNull(dto);
        assertNull(dto.getDepartmentName());
        assertEquals("Olan", dto.getName());
    }

    @Test
    @DisplayName("GET: Lista vacía cuando no hay empleados")
    void testGetAllEmpty() {
        when(employeeRepo.findAll()).thenReturn(Collections.emptyList());

        List<EmployeesObject> resultado = employeesService.getAll();

        assertTrue(resultado.isEmpty());
        assertEquals(0, resultado.size());
    }
}