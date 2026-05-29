package com.nexusbusiness;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestClient;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Date;
import java.util.TimeZone;

// INSTRUCCIÓN CRÍTICA: Escaneo del módulo POS Y la librería compartida
//@EnableWebMvc
@SpringBootApplication(scanBasePackages = {"com.nexusbusiness","com.nexussharedcore"},
        exclude = {UserDetailsServiceAutoConfiguration.class})
public class NexusBusinessApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(NexusBusinessApplication.class);
    }
    @Configuration
    public class RestClientConfig {

        @Bean
        public RestClient restClient() {
            // RestClient.create() inicializa el cliente con los convertidores de mensajes
            // y configuraciones por defecto más seguras y modernas de Spring.
            return RestClient.create();
        }
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