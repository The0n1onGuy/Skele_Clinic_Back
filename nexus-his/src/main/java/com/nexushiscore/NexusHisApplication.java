package com.nexushiscore;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.TimeZone;

// INSTRUCCIÓN CRÍTICA: Obligamos a Spring a escanear este módulo Y la librería de seguridad
@SpringBootApplication(scanBasePackages = {"com.nexushiscore", "com.nexussharedcore"})
public class NexusHisApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(NexusHisApplication.class);
    }

    // AHORA SÍ se ejecutará antes de que arranque el contexto
    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC-5"));
        System.out.println("Nexus HIS Server running in UTC-5 timezone: " + new Date());
    }

    public static void main(String[] args) {
        SpringApplication.run(NexusHisApplication.class, args);
        System.out.println("----------------------------------------");
        System.out.println("..:: NEXUS HIS (CLINIC) INITIALIZED ::..");
        System.out.println("----------------------------------------");
    }

    // Swagger exclusivo para el dominio clínico
    @Bean
    public OpenAPI clinicaOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Expediente Médico - Clínica")
                        .description("Sistema para la gestión integral de pacientes, inventario de farmacia y consultas médicas.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Nexus Development Team")
                                .email("admin@nexuscore.com")));
    }
}