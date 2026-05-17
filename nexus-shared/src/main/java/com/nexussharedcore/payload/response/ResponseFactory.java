package com.nexussharedcore.payload.response;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@ControllerAdvice
public class ResponseFactory {

    // --- CONSTRUCTOR BASE (Agnóstico de Base de Datos) ---
    private static Map<String, Object> createBaseResponse(int code, String statusName, String message) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", code);
        response.put("name", statusName);
        response.put("message", message);
        response.put("timestamp", LocalDateTime.now());
        return response;
    }

    // =========================
    // RESPUESTAS EXITOSAS
    // =========================

    public static ResponseEntity<Map<String, Object>> success(Object data) {
        Map<String, Object> response = createBaseResponse(200, HttpStatus.OK.name(), "Operación exitosa");
        response.put("data", data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public static ResponseEntity<Map<String, Object>> successMessage(String message, Object data) {
        Map<String, Object> response = createBaseResponse(200, HttpStatus.OK.name(), message);
        response.put("data", data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public static ResponseEntity<Map<String, Object>> created(String message, Object data) {
        Map<String, Object> response = createBaseResponse(201, HttpStatus.CREATED.name(), message);
        response.put("data", data);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // =========================
    // RESPUESTAS DE ERROR MANUALES
    // =========================

    public static ResponseEntity<Map<String, Object>> badRequest(String message) {
        Map<String, Object> response = createBaseResponse(400, HttpStatus.BAD_REQUEST.name(), message);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    public static ResponseEntity<Map<String, Object>> unauthorized(String message) {
        Map<String, Object> response = createBaseResponse(401, HttpStatus.UNAUTHORIZED.name(), message);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    public static ResponseEntity<Map<String, Object>> forbidden(String message) {
        Map<String, Object> response = createBaseResponse(403, HttpStatus.FORBIDDEN.name(), message);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    // =========================
    // MANEJADORES DE EXCEPCIONES GLOBALES (@ControllerAdvice)
    // =========================

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<Map<String, Object>> handleIllegalArguments(RuntimeException ex) {
        // Intercepta validaciones de negocio fallidas y las retorna como error de cliente
        Map<String, Object> response = createBaseResponse(400, HttpStatus.BAD_REQUEST.name(), ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(NoSuchElementException ex) {
        Map<String, Object> response = createBaseResponse(404, HttpStatus.NOT_FOUND.name(), ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String message = String.format("El parámetro '%s' debe ser %s", ex.getName(), ex.getRequiredType().getSimpleName());
        Map<String, Object> response = createBaseResponse(400, HttpStatus.BAD_REQUEST.name(), message);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrity(DataIntegrityViolationException ex) {
        Map<String, Object> response = createBaseResponse(409, HttpStatus.CONFLICT.name(), "No se puede completar la acción por restricciones de base de datos.");
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeExceptions(RuntimeException ex) {
        String msg = ex.getMessage() != null ? ex.getMessage() : "Fallo no identificado";

        // Log para el desarrollador en consola
        System.err.println("[EXCEPCIÓN NO CONTROLADA]: " + msg);

        if (msg.contains("Connection refused") || msg.contains("connect timed out")) {
            Map<String, Object> response = createBaseResponse(503, HttpStatus.SERVICE_UNAVAILABLE.name(), "No se pudo conectar al servicio de base de datos.");
            return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
        }

        Map<String, Object> response = createBaseResponse(500, HttpStatus.INTERNAL_SERVER_ERROR.name(), "Ocurrió un error inesperado: " + msg);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Validación de DTOs (@Valid)
    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public static ResponseEntity<Map<String, Object>> renderValidationError(BindingResult result) {
        List<String> errors = result.getFieldErrors().stream()
                .map(err -> String.format("Campo '%s': %s", err.getField(), err.getDefaultMessage()))
                .collect(Collectors.toList());

        Map<String, Object> response = createBaseResponse(400, HttpStatus.BAD_REQUEST.name(), "Error de validación de datos");
        response.put("errors", errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // =========================================================
    // ADAPTADORES DE COMPATIBILIDAD (Para GlobalExceptionHandler)
    // =========================================================
// Adaptador de legado para controladores antiguos (ej. LyR_EmployeesController)
    public static Map<String, Object> getSuccessOnGetAllResponse(Object data) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Información obtenida con éxito.");
        response.put("data", data);
        return response;
    }
    public static Map<String, Object> getErrorResponse(BindingResult result) {
        Map<String, Object> response = new HashMap<>();
        List<String> lsErrors = result.getFieldErrors().stream()
                .map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
                .collect(Collectors.toList());
        response.put("errors", lsErrors);
        return response;
    }

    public static Map<String, Object> getErrorResponse(String errorMessage) {
        Map<String, Object> response = new HashMap<>();
        response.put("errors", java.util.Collections.singletonList(errorMessage));
        return response;
    }
    public static Map<String, Object> getCreatedResponse(String message, Object data) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", message);
        response.put("data", data);
        return response;
    }

    public static Map<String, Object> getNotFoundResponse(Object object) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", String.format("%s not found.", object.getClass().getSimpleName()));
        return response;
    }

    public static Map<String, Object> getUpdateResponse(Object object) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", String.format("%s updated successfully.", object.getClass().getSimpleName()));
        response.put(object.getClass().getSimpleName().toLowerCase(), object);
        return response;
    }
}