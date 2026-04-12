package com.expedienteclinico.expedienteclinico.services.rpbi;
import com.expedienteclinico.expedienteclinico.beans.rpbi.RpbiClasificationObject;
import com.expedienteclinico.expedienteclinico.models.rpbi.RpbiClasificationModel;
import com.expedienteclinico.expedienteclinico.models.system.StatusModel;
import com.expedienteclinico.expedienteclinico.repositories.rpbi.IRpbiClasificationRepository;
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
class RpbiClasificationServiceTest {

    @Mock
    private IRpbiClasificationRepository repository;

    @InjectMocks
    private RpbiClasificationService service;

    private RpbiClasificationModel mockClass;
    private StatusModel mockStatus;

    @BeforeEach
    void setUp() {

        mockStatus = new StatusModel();
        mockStatus.setId(1L);
        mockStatus.setUuid("status-uuid-999");
        mockStatus.setStatusName("ACTIVO");

        mockClass = new RpbiClasificationModel();
        mockClass.setId(1L);
        mockClass.setUuid("status-uuid-999");
        mockClass.setName("RpbiClasification");
        mockClass.setDescription("description");
        mockClass.setColorCode("colorCode");
        mockClass.setStatus(mockStatus);
    }


    @Test
    void testGetAll_SuccessfullMapping() {
        when(repository.findAll()).thenReturn(Arrays.asList(mockClass));

        List<RpbiClasificationObject> resultados = service.getAllClasificaciones();

        assertNotNull(resultados, "La lista resultante no debe ser nula");
        assertEquals(1, resultados.size(), "Debe retornar exactamente 1 elemento");

        RpbiClasificationObject dto = resultados.get(0);

        assertEquals("status-uuid-999", dto.getUuid());
        assertEquals("RpbiClasification", dto.getName());
        assertEquals("description", dto.getDescription());
        assertEquals("colorCode", dto.getColorCode());

        assertEquals("status-uuid-999", dto.getStatusUuid());
        assertEquals("ACTIVO", dto.getEstatusName());

    }

    @Test
    void testGetAll_NullStatusProtection() {
        mockClass.setStatus(null);
        when(repository.findAll()).thenReturn(Arrays.asList(mockClass));

        List<RpbiClasificationObject> resultados = service.getAllClasificaciones();

        RpbiClasificationObject dto = resultados.get(0);

        assertEquals("RpbiClasification", dto.getName(), "Debe mapear el nombre aunque no haya estatus");
        assertNull(dto.getStatusUuid(), "El UUID de estatus debe ser nulo por protección");
        assertNull(dto.getEstatusName(), "El nombre de estatus debe ser nulo por protección");
    }

    @Test
    void testGetAll_EmptyDataBase() {
        when(repository.findAll()).thenReturn(java.util.Collections.emptyList());

        List<RpbiClasificationObject> resultados = service.getAllClasificaciones();

        assertNotNull(resultados, "El servicio nunca debe retornar null, debe retornar una lista vacía");
        assertTrue(resultados.isEmpty(), "La lista de resultados debe estar vacía");

    }

}