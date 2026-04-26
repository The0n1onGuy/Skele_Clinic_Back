package com.nexushiscore.controllers.system;

import com.nexussharedcore.payload.response.ResponseFactory;
import com.nexussharedcore.services.common.ConnectivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/system")
public class HealthController {

    @Autowired
    private ConnectivityService connectivityService;

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> connectivity = connectivityService.checkFullConnectivity();
        boolean isUp = connectivity.get("status").equals("STABLE");

        if (isUp) {
            return ResponseFactory.successMessage("Estado de infraestructura: STABLE", connectivity);
        } else {
            return ResponseFactory.badRequest("Estado de infraestructura: UNSTABLE");
        }
    }
}