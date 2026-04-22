package com.nexuscore.payload.response;

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

    public static Map< String , Object > getErrorResponse( BindingResult result){

        Map< String, Object > response = new HashMap<>();


        List< String > lsErrors = result.getFieldErrors().stream()
                .map( err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage() )
                .collect(Collectors.toList());

        response.put( "errors" , lsErrors);
        return response;

    }
    /**
     * Sobrecarga para manejar errores de seguridad o lógica de negocio (Sin BindingResult).
     * Mantiene el contrato de devolver una lista bajo la llave "errors".
     */
    public static Map<String, Object> getErrorResponse(String errorMessage) {
        Map<String, Object> response = new HashMap<>();

        List<String> lsErrors = java.util.Collections.singletonList(errorMessage);

        response.put("errors", lsErrors);
        return response;
    }

    public static Map<String, Object> getNotFoundResponse(Object object) {

        Map< String, Object > response = new HashMap<>();

        response.put("message", String.format( "%s no encontrado.", object.getClass().getSimpleName() ));

        return response;
    }

    public static Map<String, Object> getErrorToUpdateResponse(Object object) {

        Map< String, Object> response = new HashMap<>();
        response.put("message", "");
        response.put( object.getClass().getSimpleName().toLowerCase(), object);

        return response;

    }

    public static Map<String, Object> getUpdateResponse(Object object) {

        Map< String, Object > response = new HashMap<>();
        response.put("message", String.format("%s se actualió correctamente.", object.getClass().getSimpleName() ) );
        response.put( object.getClass().getSimpleName().toLowerCase(), object);

        return response;
    }

    public static Map<String, Object > getSuccessOnGetAllResponse(Object data){

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", String.format("Informacón obtenida con éxito."));
        response.put("data", data);

        return response;

    }

    // Añadir debajo de los métodos existentes en ResponseFactory
    public static Map<String, Object> getCreatedResponse(String message, Object data) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", message);
        response.put("data", data);
        return response;
    }



    // --- MÉTODOS PRIVADOS AUXILIARES (El "Motor" de la clase) ---

    private static Map<String, Object> createBaseResponse(String status, String message) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", status);
        response.put("message", message);
        response.put("timestamp", LocalDateTime.now());
        return response;
    }

    private ResponseEntity<Map<String, Object>> buildErrorResponse(String type, String message, HttpStatus status) {
        Map<String, Object> response = createBaseResponse("error", message);
        response.put("type", type);
        return new ResponseEntity<>(response, status);
    }

    // --- RESPUESTAS DE ÉXITO (Para uso en Controllers) ---

    public static ResponseEntity<Map<String, Object>> renderSuccess(String message, Object data) {
        Map<String, Object> response = createBaseResponse("success", message);
        response.put("data", data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public static ResponseEntity<Map<String, Object>> renderCreated(String message, Object data) {
        Map<String, Object> response = createBaseResponse("created", message);
        response.put("data", data);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // --- MANEJADORES DE EXCEPCIONES (Automáticos) ---

    // 400 - Errores de Validación (Uso manual en Controller con BindingResult)
    public static ResponseEntity<Map<String, Object>> renderValidationError(BindingResult result) {
        List<String> errors = result.getFieldErrors().stream()
                .map(err -> String.format("Campo '%s': %s", err.getField(), err.getDefaultMessage()))
                .collect(Collectors.toList());

        Map<String, Object> response = createBaseResponse("error", "Error de validación de datos");
        response.put("type", "Validation Error");
        response.put("errors", errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // 404 - Recurso No Encontrado
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(NoSuchElementException ex) {
        return buildErrorResponse("Not Found", ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // 400 - Error de tipo de dato (abc en lugar de un ID 123)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String message = String.format("El parámetro '%s' debe ser %s", ex.getName(), ex.getRequiredType().getSimpleName());
        return buildErrorResponse("Bad Request", message, HttpStatus.BAD_REQUEST);
    }

    // 409 - Conflicto de Integridad (Ej: borrar algo que tiene hijos)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrity(DataIntegrityViolationException ex) {
        return buildErrorResponse("Conflict", "No se puede completar la acción por restricciones de base de datos.", HttpStatus.CONFLICT);
    }

    // 500 / 503 - Fallos de Sistema o Conexión
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeExceptions(RuntimeException ex) {
        String msg = ex.getMessage() != null ? ex.getMessage() : "";

        if (msg.contains("Connection refused") || msg.contains("connect timed out")) {
            return buildErrorResponse("Service Unavailable",
                    "No se pudo conectar al servicio externo. Verifique su red.",
                    HttpStatus.SERVICE_UNAVAILABLE);
        }

        return buildErrorResponse("Internal Server Error",
                "Ocurrió un error inesperado: " + msg,
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // --- HEALTH CHECK ---
    public static ResponseEntity<Map<String, Object>> renderHealthCheck(boolean isUp) {
        Map<String, Object> response = createBaseResponse(isUp ? "UP" : "DOWN", "Health Check Status");
        response.put("service", "Expediente Clinico API");
        response.put("database", isUp ? "Connected" : "Disconnected");
        return new ResponseEntity<>(response, isUp ? HttpStatus.OK : HttpStatus.SERVICE_UNAVAILABLE);
    }
}