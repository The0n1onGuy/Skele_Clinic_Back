package com.expedienteclinico.expedienteclinico.controllers.warehouse;

import com.expedienteclinico.expedienteclinico.payload.response.ResponseFactory;
import com.expedienteclinico.expedienteclinico.models.warehouse.UnidadMedidaModel;
import com.expedienteclinico.expedienteclinico.services.warehouse.UnidadMedidaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.containsString;

@ExtendWith(MockitoExtension.class) // Inicializa los mocks de forma limpia
class UnidadMedidaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UnidadMedidaService unitService; // El "Doble"

    @InjectMocks
    private UnidadMedidaController unitController; // Donde se inyecta el doble

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        // Configuramos MockMvc en modo Standalone para test unitario puro
        mockMvc = MockMvcBuilders.standaloneSetup(unitController)
                .setControllerAdvice( new ResponseFactory())
                .build();
    }


    @Test
    @DisplayName("1.stubing básico (when/thenReturn)")
    void testGetAll() throws Exception {
        // GIVEN: Hardcoding de datos para simular la "Base de Datos"
        List<UnidadMedidaModel> lista = Arrays.asList(
                new UnidadMedidaModel(1L, "Caja", "CJ"),
                new UnidadMedidaModel(2L, "Pieza", "PZ")
        );

        // Programamos al Mock
        when(unitService.getAll()).thenReturn(lista);

        // WHEN & THEN
        mockMvc.perform(get("/api/warehouse"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Caja"));

        // Verificación básica: ¿Se llamó al metod?
        verify(unitService).getAll();
    }


    @Test
    @DisplayName("3. Verificación de comportamiento y conteo (verify/times)")
    void testGetByIdNotFound() throws Exception {
        // GIVEN: Simulamos que el ID 99 no existe
        when(unitService.getById(99L)).thenReturn(Optional.empty());

        // WHEN
        mockMvc.perform(get("/api/warehouse/99"))
                .andExpect(status().isNotFound());

        // VERIFY: Aseguramos que se llamó exactamente 1 vez a ese ID
        verify(unitService, times(1)).getById(99L);
        // Aseguramos que NUNCA se llamó al método getAll en este flujo
        verify(unitService, never()).getAll();
    }

    @Test
    @DisplayName("4. Capturador de argumentos (Capturar lo que viaja)")
    void testArgumentCaptor() throws Exception {
        // El Captor intercepta el objeto que el Controlador le envía al Service
        ArgumentCaptor<UnidadMedidaModel> captor = ArgumentCaptor.forClass(UnidadMedidaModel.class);

        UnidadMedidaModel input = new UnidadMedidaModel(null, "Ampolleta", "AMP");

        // WHEN
        mockMvc.perform(post("/api/warehouse")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)));
// todo: Investigar que hace el save en la base de datos
//         THEN: Verificamos el método save y "atrapamos" el argumento
        verify(unitService).save(captor.capture());

        UnidadMedidaModel capturado = captor.getValue();
        assertEquals("Ampolleta", capturado.getName());
        assertEquals("AMP", capturado.getAbbreviation());
    }

    @Test
    @DisplayName("Test de Error 500 - Simulación")
    void testCreateError500() throws Exception {
        // GIVEN: Un objeto que SÍ pasa las validaciones de @NotBlank
        UnidadMedidaModel validModel = new UnidadMedidaModel(null, "Kilogramo", "KG");

        // Programamos el mock para que dispare el error cuando reciba este objeto válido
        when(unitService.save(any())).thenThrow(new RuntimeException("Fallo en la base de datos"));

        // WHEN & THEN
        mockMvc.perform(post("/api/warehouse")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validModel))) // <--- Mandamos el objeto válido
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.type").value("Internal Server Error")) // Verifica la estructura de tu ResponseFactory
                .andExpect(jsonPath("$.message").value("Fallo en la base de datos"));
    }

    @Test
    @DisplayName("EXITO - POST /api/warehouse - Debería retornar 201 Created")
    void testCreateSuccess() throws Exception {
        // GIVEN
        UnidadMedidaModel input = new UnidadMedidaModel(null, "Kilogramo", "KG");
        UnidadMedidaModel saved = new UnidadMedidaModel(1L, "Kilogramo", "KG");

        when(unitService.save(any(UnidadMedidaModel.class))).thenReturn(saved);

        // WHEN & THEN
        mockMvc.perform(post("/api/warehouse")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated()) // Espera 201
                .andExpect(jsonPath("$.status").value("created"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("ERROR 400 - Validación - Nombre vacío")
    void testCreateValidationError() throws Exception {
        // GIVEN: Un objeto que viola las reglas (@NotBlank)
        UnidadMedidaModel invalidInput = new UnidadMedidaModel(null, "", "XX");

        // WHEN & THEN
        // Nota: Asegúrate que en tu Controller el metod tenga @Valid en el parámetro
        mockMvc.perform(post("/api/warehouse")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidInput)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.type").value("Validation Error"))
                .andExpect(jsonPath("$.errors").isArray());
    }

    @Test
    @DisplayName("ERROR 404 - No Encontrado - NoSuchElementException")
    void testGetByIdNotFoundException() throws Exception {
        // GIVEN: Simulamos que el servicio lanza la excepción que ResponseFactory atrapa
        when(unitService.getById(99L)).thenThrow(new NoSuchElementException("La unidad 99 no existe"));

        // WHEN & THEN
        mockMvc.perform(get("/api/warehouse/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.type").value("Not Found"))
                .andExpect(jsonPath("$.message").value("La unidad 99 no existe"));
    }

    @Test
    @DisplayName("ERROR 400 - Parámetro inválido (Type Mismatch)")
    void testBadRequestParameterType() throws Exception {
        // Intentar pasar un string "abc" donde se espera un Long id
        mockMvc.perform(get("/api/warehouse/abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.type").value("Bad Request"))
                .andExpect(jsonPath("$.message", org.hamcrest.Matchers.containsString("debe ser de tipo Long")));
    }


    //PRUEBAS DE INTERNET (AYUDAAAAAAAA)


    @Test
    @DisplayName("ERROR 503 - Simulación de caída de conexión/red")
    void testConnectionFailure() throws Exception {
        // GIVEN: El mensaje DEBE contener "Connection refused" para que el ResponseFactory lo detecte
        when(unitService.getAll()).thenThrow(new RuntimeException("Connection refused"));

        // WHEN & THEN
        mockMvc.perform(get("/api/warehouse"))
                .andExpect(status().isServiceUnavailable()) // Espera 503
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.type").value("Service Unavailable"))
                .andExpect(jsonPath("$.message", containsString("Verifique su red")));
    }


    @Test
    @DisplayName("HEALTH - Verificación de estado del sistema (UP)")
    void testHealthCheckUp() throws Exception {
        // Este test llamaría a un metod en el controller que verifique la DB
        mockMvc.perform(get("/api/warehouse/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }

}