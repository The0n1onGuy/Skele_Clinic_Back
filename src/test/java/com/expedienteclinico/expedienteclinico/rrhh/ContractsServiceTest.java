//package com.expedienteclinico.expedienteclinico.rrhh;
//
//import com.expedienteclinico.expedienteclinico.beans.rrhh.ContractObject;
//import com.expedienteclinico.expedienteclinico.models.rrhh.ContractsModel;
//import com.expedienteclinico.expedienteclinico.models.rrhh.EmployeesModel;
//import com.expedienteclinico.expedienteclinico.models.system.StatusModel;
//import com.expedienteclinico.expedienteclinico.repositories.rrhh.IContractsRepository;
//import com.expedienteclinico.expedienteclinico.services.rrhh.ContractsService;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.math.BigDecimal;
//import java.util.Collections;
//import java.util.List;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class ContractsServiceTest {
//
//    @Mock
//    private IContractsRepository contractsRepository;
//
//    @InjectMocks
//    private ContractsService contractsService;
//
//    @Test
//    @DisplayName("GET: Conversión exitosa de Contrato a DTO")
//    void testConvertToDTOSuccess() {
//
//        ContractsModel model = new ContractsModel();
//        model.setId(10L);
//        model.setUuid(UUID.randomUUID().toString());
//        model.setContractType("Planta");
//        model.setBaseSalary(new BigDecimal("25000.00"));
//
//        EmployeesModel emp = new EmployeesModel();
//        emp.setId(1L);
//        emp.setName("Olan");
//        model.setEmployee(emp);
//
//        StatusModel status = new StatusModel();
//        status.setStatusName("Active");
//        model.setStatus(status);
//
//        ContractObject dto = contractsService.convertToDTO(model);
//
//        assertNotNull(dto);
//        assertEquals("Planta", dto.getContractType());
//        assertEquals("Olan", dto.getEmployeeName());
//        assertEquals("Active", dto.getStatusName());
//        assertEquals(new BigDecimal("25000.00"), dto.getBaseSalary());
//    }
//
//    @Test
//    @DisplayName("GET: Manejo de Contrato con Empleado o Estatus NULL (Inconsistencia)")
//    void testConvertToDTONullRelations() {
//        ContractsModel model = new ContractsModel();
//        model.setContractType("Temporal");
//        model.setEmployee(null);
//        model.setStatus(null);
//
//        ContractObject dto = contractsService.convertToDTO(model);
//
//        assertNotNull(dto);
//        assertNull(dto.getEmployeeName(), "El nombre del empleado debe ser nulo si la relación no existe");
//        assertNull(dto.getStatusName(), "El nombre del estatus debe ser nulo si la relación no existe");
//        assertEquals("Temporal", dto.getContractType());
//    }
//
//    @Test
//    @DisplayName("GET: Retorno de lista vacía desde el Repositorio")
//    void testGetAllEmpty() {
//        when(contractsRepository.findAll()).thenReturn(Collections.emptyList());
//
//        List<ContractObject> resultado = contractsService.getAll();
//
//        assertNotNull(resultado);
//        assertTrue(resultado.isEmpty());
//        assertEquals(0, resultado.size());
//    }
//}