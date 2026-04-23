package com.nexuscore.payload.response;

import com.nexuscore.models.system.HttpStatusCode;
import com.nexuscore.services.system.HttpStatusService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ApiResponsefactory {

    private final HttpStatusService httpStatusService;

    public ApiResponsefactory(HttpStatusService httpStatusService) {
        this.httpStatusService = httpStatusService;
    }

    /**
     * Metodo base para construir cualquier respuesta
     */
    public Map<String, Object> buildResponse(int code, String message, Object data) {

        HttpStatusCode http = httpStatusService.getByCodeSafe(code);

        Map<String, Object> response = new HashMap<>();
        response.put("status", http.getCode());
        response.put("name", http.getName());
        response.put("description", http.getDescription());
        response.put("message", message);
        response.put("data", data);

        return response;
    }

    // =========================
    // RESPUESTAS EXITOSAS
    // =========================

    public Map<String, Object> success(Object data) {
        return buildResponse(200, "Operación exitosa", data);
    }

    public Map<String, Object> successMessage(String message, Object data) {
        return buildResponse(200, message, data);
    }

    public Map<String, Object> created(Object data) { return buildResponse(201, "Recurso creado correctamente", data);
    }

    // =========================
    // RESPUESTAS DE ERROR
    // =========================

    public Map<String, Object> error(String message) {
        return buildResponse(500, message, null);
    }

    public Map<String, Object> badRequest(String message) {
        return buildResponse(400, message, null);
    }

    public Map<String, Object> unauthorized(String message) {
        return buildResponse(401, message, null);
    }

    public Map<String, Object> forbidden(String message) {
        return buildResponse(403, message, null);
    }

    public Map<String, Object> notFound(String message) {
        return buildResponse(404, message, null);
    }
}