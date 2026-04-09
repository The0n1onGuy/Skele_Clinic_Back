package com.expedienteclinico.expedienteclinico.rrhh;

import com.expedienteclinico.expedienteclinico.beans.rrhh.DepartmentObject;
import com.expedienteclinico.expedienteclinico.models.rrhh.DepartmentsModel;
import com.expedienteclinico.expedienteclinico.models.system.StatusModel;
import com.expedienteclinico.expedienteclinico.repositories.rrhh.IDepartmentsRepository;
import com.expedienteclinico.expedienteclinico.repositories.system.IStatusRepository;
import com.expedienteclinico.expedienteclinico.services.rrhh.DepartmentsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

public class DepartmentServiceTest {
    @Mock
    private IDepartmentsRepository departmentsRepo;

    @Mock
    private IStatusRepository statusRepo;

    @InjectMocks
    private DepartmentsService departmentsService;

    private StatusModel activeStatus;
    private DepartmentsModel deptoModel;

    @BeforeEach
    void setUp() {
        activeStatus = new StatusModel();
        activeStatus.setId(1L);
        activeStatus.setStatusName("Active");

        deptoModel = new DepartmentsModel();
        deptoModel.setId(1L);
        deptoModel.setName("Sistemas");
        deptoModel.setUuid(UUID.randomUUID());
        deptoModel.setId_status(activeStatus);
        ReflectionTestUtils.setField(departmentsService, "Active", "Active");
        ReflectionTestUtils.setField(departmentsService, "Inactive", "Inactive");
    }

    @Test
    @DisplayName("Prueba de conversión de Modelo a DTO")
    void testConvertToDTO() {
        DepartmentObject dto = departmentsService.convertToDTO(deptoModel);

        assertNotNull(dto);
        assertEquals("Sistemas", dto.getName());
        assertEquals("Active", dto.getStatusName());
        assertEquals(deptoModel.getUuid().toString(), dto.getUuid());
    }

    @Test
    @DisplayName("Error al intentar guardar un departamento con nombre duplicado")
    void testSaveInfoDuplicateNameException() {
        DepartmentObject dto = new DepartmentObject();
        dto.setName("Sistemas");

        when(departmentsRepo.existsByNameIgnoreCase("Sistemas")).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            departmentsService.saveInfo(dto);
        });

        String esperado = "Error: Ya existe un departamento con el nombre 'Sistemas'.";
        assertEquals(esperado, exception.getMessage());

        verify(departmentsRepo, never()).save(any());
    }

    @Test
    @DisplayName("Guardado exitoso de un departamento")
    void testSaveInfoSuccess() {
        DepartmentObject dto = new DepartmentObject();
        dto.setName("Nuevo Depto");

        when(departmentsRepo.existsByNameIgnoreCase("Nuevo Depto")).thenReturn(false);
        when(statusRepo.findByStatusNameIgnoreCase("Active")).thenReturn(Optional.of(activeStatus));
        when(departmentsRepo.save(any(DepartmentsModel.class))).thenReturn(deptoModel);

        DepartmentObject resultado = departmentsService.saveInfo(dto);

        assertNotNull(resultado);
        verify(departmentsRepo, times(1)).save(any());
    }

    @Test
    @DisplayName("Eliminación lógica de un departamento (Cambio de estado)")
    void testDeleteInfo() {
        StatusModel inactiveStatus = new StatusModel();
        inactiveStatus.setStatusName("Inactive");

        when(departmentsRepo.findById(4L)).thenReturn(Optional.of(deptoModel));
        when(statusRepo.findByStatusNameIgnoreCase(anyString())).thenReturn(Optional.of(inactiveStatus));

        boolean deleted = departmentsService.deleteInfo(4L);

        assertTrue(deleted);
        assertEquals("Inactive", deptoModel.getId_status().getStatusName());
        verify(departmentsRepo).save(deptoModel);
    }
}
