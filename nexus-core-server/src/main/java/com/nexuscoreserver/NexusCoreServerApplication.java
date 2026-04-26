package com.nexuscoreserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import jakarta.annotation.PostConstruct;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;

import java.util.Date;
import java.util.TimeZone;

// INSTRUCCIÓN CRÍTICA: Escaneo del módulo actual Y la librería compartida
@SpringBootApplication(scanBasePackages = {"com.nexuscoreserver", "com.nexussharedcore"})
public class NexusCoreServerApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(NexusCoreServerApplication.class);
    }

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC-5"));
        System.out.println("Nexus Core Server running in UTC-5 timezone: " + new Date());
    }

    public static void main(String[] args) {
        SpringApplication.run(NexusCoreServerApplication.class, args);
        System.out.println("----------------------------------------");
        System.out.println("..:: NEXUS CORE (CONTROL) INITIALIZED ::..");
        System.out.println("----------------------------------------");
    }

    // Swagger exclusivo para el dominio de Control Global
    @Bean
    public OpenAPI coreOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Nexus Core - Control Plane API")
                        .description("Sistema de administración global, identidad, 2FA TOTP y orquestación de Tenants.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Nexus Infrastructure Team")
                                .email("admin@nexuscore.com")));
    }
}