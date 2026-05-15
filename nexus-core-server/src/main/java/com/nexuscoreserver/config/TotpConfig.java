package com.nexuscoreserver.config;

import dev.samstevens.totp.code.*;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TotpConfig {

    @Bean
    public TimeProvider timeProvider() {
        return new SystemTimeProvider();
    }

    @Bean
    public CodeGenerator codeGenerator() {
        return new DefaultCodeGenerator();
    }

    @Bean
    public CodeVerifier codeVerifier() {
        // Maneja la validación de los 6 dígitos con un margen de error de 1 intervalo (30s)
        return new DefaultCodeVerifier(codeGenerator(), timeProvider());
    }
}