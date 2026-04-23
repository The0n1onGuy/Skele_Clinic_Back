package com.nexuscore.rrhh;

import com.nexuscore.beans.rrhh.PositionsObject;
import com.nexuscore.models.rrhh.PositionsModel;
import com.nexuscore.models.system.StatusModel;
import com.nexuscore.repositories.rrhh.IPositionsRepository;
import com.nexuscore.services.rrhh.PositionsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PositionsServiceTest {

    @Mock
    private IPositionsRepository positionsRepo;

    @InjectMocks
    private PositionsService positionsService;

    @Test
    @DisplayName("GET: Conversión exitosa de Puesto a DTO")
    void testConvertToDTOSuccess() {
        PositionsModel model = new PositionsModel();
        model.setId(1L);
        model.setName("Gerente");
        model.setDescription("Encargado de oficina");
        model.setUuid(UUID.randomUUID());

        StatusModel status = new StatusModel();
        status.setStatusName("Active");
        model.setId_status(status);

        PositionsObject dto = positionsService.convertToDTO(model);

        assertNotNull(dto);
        assertEquals("Gerente", dto.getName());
        assertEquals("Active", dto.getStatusName());
    }

    @Test
    @DisplayName("GET: Manejo de Puesto con Estatus NULL (Inconsistencia de BD)")
    void testConvertToDTONullStatus() {
        PositionsModel model = new PositionsModel();
        model.setName("Secretaria");
        model.setId_status(null);

        PositionsObject dto = positionsService.convertToDTO(model);

        assertNotNull(dto);
        assertNull(dto.getStatusName(), "El nombre del estatus debe ser nulo si la relación no existe");
        assertEquals("Secretaria", dto.getName());
    }

    @Test
    @DisplayName("GET: Retorno de lista vacía desde el repositorio")
    void testGetAllEmpty() {
        when(positionsRepo.findAll()).thenReturn(Collections.emptyList());

        List<PositionsObject> resultado = positionsService.getAll();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        assertEquals(0, resultado.size());
    }
}