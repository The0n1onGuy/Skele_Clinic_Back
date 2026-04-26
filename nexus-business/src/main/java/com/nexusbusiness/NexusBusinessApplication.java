package com.nexusbusiness;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import jakarta.annotation.PostConstruct;

import java.util.Date;
import java.util.TimeZone;

// INSTRUCCIÓN CRÍTICA: Escaneo del módulo POS Y la librería compartida
@SpringBootApplication(scanBasePackages = {"com.nexusbusiness", "com.nexussharedcore"})
public class NexusBusinessApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(NexusBusinessApplication.class);
    }

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC-5"));
        System.out.println("Nexus Business POS running in UTC-5 timezone: " + new Date());
    }

    public static void main(String[] args) {
        SpringApplication.run(NexusBusinessApplication.class, args);
        System.out.println("----------------------------------------");
        System.out.println("..:: NEXUS BUSINESS (POS) INITIALIZED ::..");
        System.out.println("----------------------------------------");
    }
}