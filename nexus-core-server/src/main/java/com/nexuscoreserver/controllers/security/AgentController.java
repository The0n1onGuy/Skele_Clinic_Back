package com.nexuscoreserver.controllers.security;

import com.nexuscoreserver.services.system.AgentService;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/system/logs")
@CrossOrigin(origins = "*") // Allows your Vue.js frontend to call this
public class AgentController {
    private final AgentService logService;

    public AgentController(AgentService logService) {
        this.logService = logService;
    }

    // GET: http://localhost:2026/api/system/logs/remote
    @GetMapping("/all")
    public ResponseEntity<String> getRemoteLogs() {
        try {
            String logs = logService.fetchAllLogs();
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error connecting to Log Agent: " + e.getMessage());
        }

    }
    @Scheduled(cron = "0 0 0 * * *") // Executes midgnigth
    public void autoCleanupLogs() {
        // Para producción: 525600 minutos (1 año)
        // Para tus pruebas: podrías usar una variable de configuración
        int minutes = 525600;
        logService.callCleanup(minutes);
    }
}
