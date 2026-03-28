//package com.expedienteclinico.expedienteclinico.rrhh;
//
//import com.expedienteclinico.expedienteclinico.beans.rrhh.SchedulesObject;
//import com.expedienteclinico.expedienteclinico.models.rrhh.EmployeesModel;
//import com.expedienteclinico.expedienteclinico.models.rrhh.SchedulesModel;
//import com.expedienteclinico.expedienteclinico.models.system.StatusModel;
//import com.expedienteclinico.expedienteclinico.repositories.rrhh.ISchedulesRepository;
//import com.expedienteclinico.expedienteclinico.services.rrhh.SchedulesService;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Collections;
//import java.util.List;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class SchedulesServiceTest {
//
//    @Mock
//    private ISchedulesRepository schedulesRepository;
//
//    @InjectMocks
//    private SchedulesService schedulesService;
//
//    @Test
//    @DisplayName("GET: Conversión exitosa de Horario a DTO")
//    void testConvertToDTOSuccess() {
//        SchedulesModel model = new SchedulesModel();
//        model.setDayOfWeek("Lunes");
//        model.setStartTime("08:00");
//        model.setEndTime("16:00");
//        model.setUuid(UUID.randomUUID());
//
//        EmployeesModel emp = new EmployeesModel();
//        emp.setName("Olan");
//        emp.setPatname("M.");
//        emp.setMatname("S.");
//        model.setEmployee(emp);
//
//        StatusModel status = new StatusModel();
//        status.setStatusName("Active");
//        model.setStatus(status);
//
//        SchedulesObject dto = schedulesService.convertToDTO(model);
//
//        assertNotNull(dto);
//        assertEquals("Lunes", dto.getDayOfWeek());
//        assertTrue(dto.getEmployeeFullName().contains("Olan"));
//        assertEquals("Active", dto.getStatusName());
//    }
//
//    @Test
//    @DisplayName("GET: Manejo de relaciones nulas (Empleado o Estatus inexistente)")
//    void testConvertToDTONullRelations() {
//        SchedulesModel model = new SchedulesModel();
//        model.setDayOfWeek("Viernes");
//        model.setEmployee(null);
//        model.setStatus(null);
//
//        SchedulesObject dto = schedulesService.convertToDTO(model);
//
//        assertNotNull(dto);
//        assertNull(dto.getEmployeeFullName());
//        assertNull(dto.getStatusName());
//        assertEquals("Viernes", dto.getDayOfWeek());
//    }
//
//    @Test
//    @DisplayName("GET: Retorno de lista vacía desde el repositorio")
//    void testGetAllEmpty() {
//        when(schedulesRepository.findAll()).thenReturn(Collections.emptyList());
//
//        List<SchedulesObject> resultado = schedulesService.getAll();
//
//        assertNotNull(resultado);
//        assertTrue(resultado.isEmpty());
//    }
//}