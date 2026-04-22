package com.nexuscore.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest // Carga el contexto completo para validar la seguridad real
@ActiveProfiles("test")
class SwaggerSecurityTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        // Configuramos MockMvc manualmente inyectando la seguridad de Spring
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    @DisplayName("Swagger debe denegar acceso a usuarios no autenticados")
    void testSwaggerIsRestricted() throws Exception {
        // Intentamos entrar a la UI de Swagger sin token ni sesión
        mockMvc.perform(get("/swagger-ui/index.html"))
                .andExpect(status().isUnauthorized()); // Esperamos 401 porque no hay token
    }
}