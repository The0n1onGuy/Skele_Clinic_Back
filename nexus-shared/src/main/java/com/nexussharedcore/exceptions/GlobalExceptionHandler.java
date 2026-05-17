package com.nexussharedcore.exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.security.core.AuthenticationException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.nexussharedcore.payload.response.ResponseFactory;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ==========================================
    // 1. DOMINIO DE SEGURIDAD (HTTP 401 / 403)
    // ==========================================

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Map<String, Object>> handleExpiredJwtException(ExpiredJwtException ex) {
        return new ResponseEntity<>(
                ResponseFactory.getErrorResponse("El token ha expirado. Inicie sesión nuevamente"),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<Map<String, Object>> handleSignatureException(SignatureException ex) {
        return new ResponseEntity<>(
                ResponseFactory.getErrorResponse("Firma del Token inválida. Acceso denegado."),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDeniedException(AccessDeniedException ex) {
        return new ResponseEntity<>(
                ResponseFactory.getErrorResponse("Acceso denegado. No tiene los privilegios para realizar esta acción."),
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, Object>> handleAuthenticationException(AuthenticationException ex) {
        return new ResponseEntity<>(
                ResponseFactory.getErrorResponse("No autorizado: Debe proporcionar un token de autenticación válido."),
                HttpStatus.UNAUTHORIZED
        );
    }

    // CORRECCIÓN CRÍTICA: Se retiró IllegalArgumentException de este bloque
    @ExceptionHandler({MalformedJwtException.class, UnsupportedJwtException.class})
    public ResponseEntity<Map<String, Object>> handleMalformedJwtException(Exception ex) {
        return new ResponseEntity<>(
                ResponseFactory.getErrorResponse("Estructura del token no válida o soportada."),
                HttpStatus.UNAUTHORIZED
        );
    }

    // ==========================================
    // 2. DOMINIO DE VALIDACIÓN DE DATOS (HTTP 400)
    // ==========================================

    // Interceptor para errores de Jakarta Validation (@Valid en los DTOs)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getDefaultMessage()) // Extrae el mensaje que definiste en el @NotBlank
                .collect(Collectors.toList());

        // Retorna un HTTP 400 explícito con la lista de campos faltantes
        return new ResponseEntity<>(
                ResponseFactory.getErrorResponse(String.join(", ", errors)),
                HttpStatus.BAD_REQUEST
        );
    }

    // Interceptor para errores de negocio controlados (ej. "El rol no existe")
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>(
                ResponseFactory.getErrorResponse(ex.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    // ==========================================
    // 3. FALLBACK DE INFRAESTRUCTURA (HTTP 500)
    // ==========================================

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        ex.printStackTrace();

        // Si el error viene propagado desde otro microservicio vía RestClient, mostramos ese error
        if (ex.getMessage() != null && ex.getMessage().contains("Rechazo de CORE")) {
            return new ResponseEntity<>(
                    ResponseFactory.getErrorResponse(ex.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }

        return new ResponseEntity<>(
                ResponseFactory.getErrorResponse("Ha ocurrido un error inesperado en el servidor."),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}