package com.nexuscoreserver.config;

import com.nexuscoreserver.audit.HttpLoggingInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // define configuraciones para el contexto de Spring
public class Webconfig implements WebMvcConfigurer {

    // Declaración del interceptor que será inyectado por Spring
    private final HttpLoggingInterceptor interceptor;

    // Constructor para inyectar el interceptor (inyección por constructor)
    public Webconfig(HttpLoggingInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    // Metodo sobrescrito para registrar interceptores en la aplicación
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // Se agrega el interceptor al registro
        registry.addInterceptor(interceptor)

                // Se especifica que el interceptor solo actuará sobre rutas que comiencen con /api/
                // El patrón /** indica que aplica a todos los subniveles dentro de /api
                .addPathPatterns("/api/**"); // solo APIs
    }
}