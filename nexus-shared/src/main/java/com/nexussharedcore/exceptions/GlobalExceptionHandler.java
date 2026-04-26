package com.nexussharedcore.exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.security.core.AuthenticationException;
import java.util.Map;
import com.nexussharedcore.payload.response.ResponseFactory;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Manejo del Token Expirado (Lanzado desde el Filtro)
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Map<String, Object>> handleExpiredJwtException(ExpiredJwtException ex) {
        return new ResponseEntity<>(
                ResponseFactory.getErrorResponse("El token ha expirado. Inicie sesión nuevamente"),
                HttpStatus.UNAUTHORIZED
            );
        }
    // 2. Manejo de Firma Inválida (Lanzado desde el Filtro)
    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<Map<String, Object>> handleSignatureException(SignatureException ex) {
        return new ResponseEntity<>(
                ResponseFactory.getErrorResponse("Firma del Token inválida. Acceso denegado."),
                HttpStatus.UNAUTHORIZED
        );
    }

    // 3. Manejo de Permisos Insuficientes (Lanzado por Spring Security rules)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDeniedException(AccessDeniedException ex) {
        return new ResponseEntity<>(
                ResponseFactory.getErrorResponse("Acceso denegado. No tiene los privilegios para realizar esta acción."),
                HttpStatus.FORBIDDEN
        );
    }

    // Maneja el 401 genérico: Peticiones sin token o credenciales nulas
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, Object>> handleAuthenticationException(AuthenticationException ex) {
        return new ResponseEntity<>(
                ResponseFactory.getErrorResponse("No autorizado: Debe proporcionar un token de autenticación válido."),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler({MalformedJwtException.class, UnsupportedJwtException.class, IllegalArgumentException.class})
    public ResponseEntity<Map<String, Object>> handleMalformedJwtException(Exception ex) {

        return new ResponseEntity<>(
            ResponseFactory.getErrorResponse("Estructura del token no válida o soportada."),
            HttpStatus.UNAUTHORIZED
        );

    }

    // Fallbakc de seguridad
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        ex.printStackTrace();
        ex.getMessage();
        return new ResponseEntity<>(
                ResponseFactory.getErrorResponse("Ha ocurrido un error inesperado en el servidor."),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

}