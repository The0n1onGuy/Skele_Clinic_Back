package com.nexuscoreserver.services.system;

import com.nexuscoreserver.models.system.HttpStatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.util.HashMap;
import java.util.Map;

@Service
public class Systemlogservice {

    private static final Logger logger = LoggerFactory.getLogger(Systemlogservice.class);
    private final HttpStatusService httpStatusService;
    private final RestTemplate restTemplate = new RestTemplate();


    // Remote Agent
    // Method with test server
    //private final String AGENT_URL = "http://localhost:8081/api/agent/receive";

    // Method with Oracle Server
    private final String AGENT_URL = "http://159.54.151.135:8081/api/agent/receive";

    private final String AGENT_SECRET = "NexusAccess_Secret_2026";

    public Systemlogservice(HttpStatusService httpStatusService) {
        this.httpStatusService = httpStatusService;
    }

    /**
     * Main log method compatible with HttpLoggingFilter
     *
     * * @param code HTTP Status Code (e.g., 200, 404, 500)
     * @param method HTTP Method (GET, POST, etc.)
     * @param uri The requested endpoint path
     * @param duration Request execution time in milliseconds
     */
    public void log(int code, String method, String uri, long duration) {

        // Retrieve detailed status information from the Database
        HttpStatusCode http = httpStatusService.getByCodeSafe(code);

        // Standardize "Operation" and "Detail" strings for the Remote Agent
        String operation = method + " " + uri;
        String detail = String.format("Description: %s | Duration: %d ms",
                http.getDescription(), duration);

        // Local Console Logging (SLF4J)
        // Uses different log levels based on the HTTP status code range
        String consoleMsg = String.format("[HTTP %d - %s] | Operation: %s | Time: %d ms",
                code, http.getName(), operation, duration);

        if (code >= 500) {
            logger.error(consoleMsg + " | Full Detail: " + detail);
        } else if (code >= 400) {
            logger.warn(consoleMsg);
        } else {
            logger.info(consoleMsg);
        }

        // Remote Agent Dispatch
        // Sends the log as a JSON payload to the Node.js agent
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("code", code);
            payload.put("status", http.getName());
            payload.put("operation", operation);
            payload.put("detail", detail);
            payload.put("timestamp", java.time.LocalDateTime.now().toString());

            // Set up Security Headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("X-Nexus-Secret", AGENT_SECRET);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

            // ASYNC EXECUTION: To prevent blocking the user's response time
            new Thread(() -> {
                try {
                    restTemplate.postForEntity(AGENT_URL, entity, String.class);
                } catch (Exception e) {
                    // Fail silently for remote logging to maintain API stability
                }
            }).start();

        } catch (Exception e) {
            logger.debug("Failed to prepare remote log payload: " + e.getMessage());
        }
    }
}