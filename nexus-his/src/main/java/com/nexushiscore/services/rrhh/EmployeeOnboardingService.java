package com.nexushiscore.services.rrhh;

import com.nexushiscore.beans.rrhh.EmployeeOnboardingRequestObject;
import com.nexushiscore.models.rrhh.DepartmentsModel;
import com.nexushiscore.models.rrhh.EmployeesModel;
import com.nexushiscore.models.rrhh.PositionsModel;
import com.nexushiscore.models.system.StatusModel;
import com.nexushiscore.repositories.rrhh.IDepartmentsRepository;
import com.nexushiscore.repositories.rrhh.IEmployeesRepository;
import com.nexushiscore.repositories.rrhh.IPositionsRepository;
import com.nexushiscore.repositories.system.IStatusRepository;
import com.nexussharedcore.security.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class EmployeeOnboardingService {

    // Extraemos la URL base del application.yml (ej. http://localhost:8080)
    @Value("${nexus.core.url:http://localhost:8080}")
    private String coreServiceUrl;

    @Autowired private IStatusRepository statusRepository;
    @Autowired private IDepartmentsRepository departmentsRepository;
    @Autowired private IPositionsRepository positionsRepository;
    @Autowired private IEmployeesRepository employeesRepository;

    @Autowired private RestClient restClient;

    public void onboardNewEmployee(EmployeeOnboardingRequestObject request, String tenantId, String authToken) {
        String sharedUuid = UUID.randomUUID().toString();

        if (request.getRequiresSystemAccess() != null && request.getRequiresSystemAccess()) {

            // RUTA A: COMUNICACIÓN SÍNCRONA CON CORE
            createSystemUserViaCore(request, authToken, tenantId);

            try {
                TenantContext.setCurrentTenant(tenantId);
                insertIntoTenantJPA(request, sharedUuid);
            } catch (Exception e) {
                // NOTA ARQUITECTÓNICA: Si falla aquí, el usuario ya se creó en CORE.
                // En un futuro, aquí iría una llamada HTTP DELETE a CORE para hacer Rollback.
                e.printStackTrace();
                throw new RuntimeException("Fallo al guardar el perfil clínico local. Revisa la consistencia en CORE.", e);
            } finally {
                TenantContext.clear();
            }
        } else {
            // RUTA B: EXCLUSIVO DE TENANT
            try {
                TenantContext.setCurrentTenant(tenantId);
                insertIntoTenantJPA(request, sharedUuid);
            } finally {
                TenantContext.clear();
            }
        }
    }

    private void createSystemUserViaCore(EmployeeOnboardingRequestObject request, String authToken, String tenantId) {
        if (request.getUsername() == null || request.getPassword() == null || request.getRoleName() == null) {
            throw new IllegalArgumentException("Usuario, contraseña y rol son obligatorios si requiresSystemAccess es true");
        }

        // 1. Limpieza criptográfica estricta
        String cleanToken = authToken.replace("Bearer ", "").replaceAll("\\s+", "");
        String endpoint = coreServiceUrl + "/api/core/v1/system-users/create";

        // 2. Construcción del Payload
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("userName", request.getUsername());
        requestBody.put("password", request.getPassword());
        requestBody.put("roleName", request.getRoleName());
        requestBody.put("tenantId", tenantId);

        System.out.println("====== OUTBOUND HTTP DEBUG (RestClient) ======");
        System.out.println("DESTINO: " + endpoint);
        System.out.println("AUTHORIZATION HEADER: [Bearer " + cleanToken + "]");
        System.out.println("==============================================");

        // 3. Ejecución Fluida Síncrona
        restClient.post()
                .uri(endpoint)
                .header("Authorization", "Bearer " + cleanToken)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .body(requestBody)
                .retrieve()
                .onStatus(org.springframework.http.HttpStatusCode::isError, (req, res) -> {
                    // Interceptamos cualquier error (4xx o 5xx) y extraemos el mensaje real de CORE
                    // sin que Spring lo enmascare con excepciones genéricas.
                    String coreErrorMessage = new String(res.getBody().readAllBytes());
                    throw new RuntimeException("Rechazo de CORE [HTTP " + res.getStatusCode() + "]: " + coreErrorMessage);
                })
                .toBodilessEntity(); // Equivalente a esperar un 201 Created sin mapear un Body de retorno complejo
    }

    private void insertIntoTenantJPA(EmployeeOnboardingRequestObject request, String uuidString) {
        EmployeesModel employee = new EmployeesModel();
        employee.setUuid(UUID.fromString(uuidString));
        employee.setName(request.getName());
        employee.setPatname(request.getPatname());
        employee.setMatname(request.getMatname() != null ? request.getMatname() : "");
        employee.setCurp(request.getCurp());
        employee.setRfc(request.getRfc());
        employee.setGender(request.getGender());
        employee.setDatebirth(LocalDate.parse(request.getDatebirth()));
        employee.setDatereg(LocalDate.now());

        PositionsModel position = positionsRepository.findById(request.getIdPosition())
                .orElseThrow(() -> new RuntimeException("Posición no encontrada: " + request.getIdPosition()));
        employee.setId_position(position);

        DepartmentsModel department = departmentsRepository.findById(request.getIdDepartment())
                .orElseThrow(() -> new RuntimeException("Departamento no encontrado: " + request.getIdDepartment()));
        employee.setId_department(department);

        StatusModel activeStatus = statusRepository.findByStatusNameIgnoreCase("Active")
                .orElseThrow(() -> new RuntimeException("Estado 'Active' no encontrado en el diccionario."));
        employee.setId_status(activeStatus);

        employeesRepository.save(employee);
    }
}