package com.nexushiscore.services.rrhh;

import com.nexushiscore.beans.rrhh.EmployeeOnboardingRequestObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.Map;

/**
 * Servicio para delegar el Onboarding de empleados de forma asíncrona y consistente.
 * Refactorizado para eliminar el doble guardado síncrono. Ahora delega el inicio de la SAGA
 * al Core Server, el cual registra la operación en el Outbox Maestre.
 */
@Slf4j
@Service
public class EmployeeOnboardingService {

    @Value("${nexus.core.url:http://localhost:8080}")
    private String coreServiceUrl;

    @Autowired 
    private RestClient restClient;

    public void onboardNewEmployee(EmployeeOnboardingRequestObject request, String tenantId, String authToken) {
        log.info(">>> [HIS SERVICE] Iniciando delegación de Onboarding SAGA para el empleado '{}' en tenant '{}'...", 
                request.getName(), tenantId);

        // 1. Limpieza criptográfica estricta de la cabecera Bearer
        String cleanToken = authToken.replace("Bearer ", "").replaceAll("\\s+", "");
        String endpoint = coreServiceUrl + "/api/core/v1/system-users/onboard";

        // 2. Construcción del Payload Completo de Onboarding para el Core Plane
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("requiresSystemAccess", request.getRequiresSystemAccess());
        requestBody.put("username", request.getUsername());
        requestBody.put("password", request.getPassword());
        requestBody.put("roleName", request.getRoleName());
        requestBody.put("tenantId", tenantId);
        requestBody.put("name", request.getName());
        requestBody.put("patname", request.getPatname());
        requestBody.put("matname", request.getMatname());
        requestBody.put("curp", request.getCurp());
        requestBody.put("idDepartment", request.getIdDepartment());
        requestBody.put("idPosition", request.getIdPosition());
        requestBody.put("rfc", request.getRfc());
        requestBody.put("gender", request.getGender());
        requestBody.put("datebirth", request.getDatebirth());

        log.info(">>> Delegando inicio de SAGA al Core: {}", endpoint);

        // 3. Ejecución de la llamada REST síncrona de delegación
        try {
            restClient.post()
                    .uri(endpoint)
                    .header("Authorization", "Bearer " + cleanToken)
                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                    .body(requestBody)
                    .retrieve()
                    .onStatus(org.springframework.http.HttpStatusCode::isError, (req, res) -> {
                        String coreErrorMessage = new String(res.getBody().readAllBytes());
                        throw new RuntimeException("Rechazo de CORE [HTTP " + res.getStatusCode() + "]: " + coreErrorMessage);
                    })
                    .toBodilessEntity();
            
            log.info("<<< [SAGA DELEGADA ÉXITO] La SAGA ha sido registrada en el Outbox central con éxito.");
            
        } catch (Exception e) {
            log.error("[FALLO INICIACIÓN SAGA] Error al contactar al Core Server: {}", e.getMessage());
            throw new RuntimeException("Fallo al iniciar el onboarding en el plano de control (Core). Detalle: " + e.getMessage(), e);
        }
    }
}