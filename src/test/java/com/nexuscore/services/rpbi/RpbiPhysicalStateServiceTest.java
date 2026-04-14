package com.nexuscore.services.rpbi;

import com.nexuscore.beans.rpbi.RpbiPhysicalStateObject;
import com.nexuscore.models.system.StatusModel;
import com.nexuscore.models.rpbi.RpbiPhysicalStateModel;
import com.nexuscore.repositories.rpbi.IRpbiPhysicalStateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RpbiPhysicalStateServiceTest {

    @Mock
    private IRpbiPhysicalStateRepository repository;

    @InjectMocks
    private RpbiPhysicalStateService service;

    private RpbiPhysicalStateModel mockState;
    private StatusModel mockStatus;

    @BeforeEach
    void setUp() {
        mockStatus = new StatusModel();
        mockStatus.setId(1L);
        mockStatus.setUuid("status-uuid-999");
        mockStatus.setStatusName("ACTIVO");

        mockState = new RpbiPhysicalStateModel();
        mockState.setId(1L);
        mockState.setUuid("state-uuid-777");
        mockState.setName("Líquido");
        mockState.setMeasureUnit("L");
        mockState.setStatus(mockStatus);
    }

    @Test
    void testGetAll_SuccessfullMapping() {
        when(repository.findAll()).thenReturn(Arrays.asList(mockState));

        List<RpbiPhysicalStateObject> resultados = service.getAll();

        assertNotNull(resultados, "La lista resultante no debe ser nula");
        assertEquals(1, resultados.size(), "Debe retornar exactamente 1 elemento");

        RpbiPhysicalStateObject dto = resultados.get(0);

        assertEquals("state-uuid-777", dto.getUuid());
        assertEquals("Líquido", dto.getName());
        assertEquals("L", dto.getMeasureUnit());

        assertEquals("status-uuid-999", dto.getStatusUuid());
        assertEquals("ACTIVO", dto.getEstatusName());

    }

    @Test
    void testGetAll_NullStatusProtection() {
        mockState.setStatus(null);
        when(repository.findAll()).thenReturn(Arrays.asList(mockState));

        List<RpbiPhysicalStateObject> resultados = service.getAll();

        RpbiPhysicalStateObject dto = resultados.get(0);

        assertEquals("Líquido", dto.getName(), "Debe mapear el nombre aunque no haya estatus");
        assertNull(dto.getStatusUuid(), "El UUID de estatus debe ser nulo por protección");
        assertNull(dto.getEstatusName(), "El nombre de estatus debe ser nulo por protección");
    }

    @Test
    void testGetAll_EmptyDataBase() {
        when(repository.findAll()).thenReturn(java.util.Collections.emptyList());

        List<RpbiPhysicalStateObject> resultados = service.getAll();

        assertNotNull(resultados, "El servicio nunca debe retornar null, debe retornar una lista vacía");
        assertTrue(resultados.isEmpty(), "La lista de resultados debe estar vacía");

    }
}