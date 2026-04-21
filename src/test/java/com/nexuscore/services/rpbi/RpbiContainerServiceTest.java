package com.nexuscore.services.rpbi;
import com.nexuscore.beans.rpbi.RpbiContainerObject;
import com.nexuscore.models.system.StatusModel;
import com.nexuscore.models.rpbi.RpbiContainerModel;
import com.nexuscore.repositories.rpbi.IRpbiContainerRepository;
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
class RpbiContainerServiceTest {


    @Mock
    private IRpbiContainerRepository repository;

    @InjectMocks
    private RpbiContainerService service;

    private RpbiContainerModel mockClass;
    private StatusModel mockStatus;

    @BeforeEach
    void setUp() {

        mockStatus = new StatusModel();
        mockStatus.setId(1L);
        mockStatus.setUuid("status-uuid-999");
        mockStatus.setStatusName("ACTIVO");

        mockClass = new RpbiContainerModel();
        mockClass.setId(1L);
        mockClass.setUuid("class-uuid-999");
        mockClass.setName("Bottle");
        mockClass.setDescription("Plastic-Bottle");
        mockClass.setStatus(mockStatus);

    }

    @Test
    void testGetAll_SuccessfullMapping() {
        when(repository.findAll()).thenReturn(Arrays.asList(mockClass));

        List<RpbiContainerObject> resultados = service.getAll();

        assertNotNull(resultados, "La lista resultante no debe ser nula");
        assertEquals(1, resultados.size(), "Debe retornar exactamente 1 elemento");

        RpbiContainerObject dto = resultados.get(0);

        assertEquals("class-uuid-999", dto.getUuid());
        assertEquals("Bottle", dto.getName());
        assertEquals("Plastic-Bottle", dto.getDescription());


        assertEquals("status-uuid-999", dto.getStatusUuid());
        assertEquals("ACTIVO", dto.getEstatusName());

    }

    @Test
    void testGetAll_NullStatusProtection() {
        mockClass.setStatus(null);
        when(repository.findAll()).thenReturn(Arrays.asList(mockClass));

        List<RpbiContainerObject> resultados = service.getAll();

        RpbiContainerObject dto = resultados.get(0);

        assertEquals("Bottle", dto.getName(), "Debe mapear el nombre aunque no haya estatus");
        assertNull(dto.getStatusUuid(), "El UUID de estatus debe ser nulo por protección");
        assertNull(dto.getEstatusName(), "El nombre de estatus debe ser nulo por protección");
    }

    @Test
    void testGetAll_EmptyDataBase() {
        when(repository.findAll()).thenReturn(java.util.Collections.emptyList());

        List<RpbiContainerObject> resultados = service.getAll();

        assertNotNull(resultados, "El servicio nunca debe retornar null, debe retornar una lista vacía");
        assertTrue(resultados.isEmpty(), "La lista de resultados debe estar vacía");

    }
}