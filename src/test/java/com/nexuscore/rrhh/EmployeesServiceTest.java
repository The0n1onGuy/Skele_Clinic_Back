package com.nexuscore.rrhh;
import com.nexuscore.models.rrhh.EmployeesModel;
import com.nexuscore.models.system.StatusModel;
import com.nexuscore.repositories.rrhh.IDepartmentsRepository;
import com.nexuscore.repositories.rrhh.IEmployeesRepository;
import com.nexuscore.repositories.rrhh.IPositionsRepository;
import com.nexuscore.repositories.system.IStatusRepository;
import com.nexuscore.services.rrhh.EmployeesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

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

//    @Test
//    @DisplayName("GET: Conversión exitosa con todas las llaves foráneas")
//    void testConvertToDTOSuccess() {
//        EmployeesModel model = new EmployeesModel();
//        model.setName("Olan");
//        model.setUuid(UUID.randomUUID());
//
//        DepartmentsModel dept = new DepartmentsModel();
//        dept.setName("Sistemas");
//        model.setId_department(dept);
//
//        EmployeesObject dto = employeesService.convertToDTO(model);
//
//        assertEquals("Olan", dto.getName());
//        assertEquals("Sistemas", dto.getDepartmentName());
//    }

//    @Test
//    @DisplayName("GET: Manejo de error cuando el Departamento es NULL")
//    void testConvertToDTONullDepartment() {
//        EmployeesModel model = new EmployeesModel();
//        model.setName("Olan");
//        model.setId_department(null);
//
//        EmployeesObject dto = employeesService.convertToDTO(model);
//
//        assertNotNull(dto);
//        assertNull(dto.getDepartmentName());
//        assertEquals("Olan", dto.getName());
//    }

//    @Test
//    @DisplayName("GET: Lista vacía cuando no hay empleados")
//    void testGetAllEmpty() {
//        when(employeeRepo.findAll()).thenReturn(Collections.emptyList());
//
//        List<EmployeesObject> resultado = employeesService.getAll();
//
//        assertTrue(resultado.isEmpty());
//        assertEquals(0, resultado.size());
//    }
}