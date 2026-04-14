package com.expedienteclinico.expedienteclinico.controllers.system;

import com.expedienteclinico.expedienteclinico.beans.system.TenantProvisioningRequestObject;
import com.expedienteclinico.expedienteclinico.services.system.TenantProvisioningService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/master/tenants")
public class TenantAdminController {

    @Autowired
    private TenantProvisioningService tenantProvisioningService;

    @PostMapping("/provision")
    public ResponseEntity<?> provisionTenant(@RequestBody @Valid TenantProvisioningRequestObject request) {
        String tenantName = request.getTenantKey();

        // Validación estructural temprana
        if (tenantName == null || tenantName.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "El nombre del hospital es obligatorio."));
        }

        Map<String, Object> response = new HashMap<>();
        try {
            // Forzamos minúsculas para mantener la convención de nombres en MySQL
            String normalizedTenant = tenantName.trim().toLowerCase();
            request.setTenantKey(normalizedTenant);
            // Disparamos el orquestador
            tenantProvisioningService.provisionNewTenant(request);

            response.put("mensaje", "Hospital '" + normalizedTenant + "' aprovisionado y listo para operar.");
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (Exception e) {
            e.printStackTrace(); // Trazabilidad crítica
            response.put("mensaje", "Error al aprovisionar la infraestructura del cliente.");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}