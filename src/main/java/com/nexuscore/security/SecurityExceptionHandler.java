package com.nexuscore.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
public class SecurityExceptionHandler implements AuthenticationEntryPoint, AccessDeniedHandler {

    private final HandlerExceptionResolver resolver;

    // Inyectamos el puente hacia la capa MVC
    public SecurityExceptionHandler(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    /**
     * Maneja el error 401 (No Autorizado): Ocurre cuando un usuario intenta acceder
     * a una ruta protegida SIN enviar un token en el encabezado.
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        // Redirige la excepción hacia tu GlobalExceptionHandler
        resolver.resolveException(request, response, null, authException);
    }

    /**
     * Maneja el error 403 (Acceso Denegado): Ocurre cuando el usuario tiene un token válido,
     * pero sus roles no coinciden con los requeridos (ej. MORGUE intentando entrar a ALMACEN).
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException, ServletException {
        // Redirige la excepción hacia tu GlobalExceptionHandler
        resolver.resolveException(request, response, null, accessDeniedException);
    }
}