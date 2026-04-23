package com.nexuscore.services.system;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class AgentService {
    private final String AGENT_URL = "http://159.54.151.135:8081/api/agent";
    private final String SECRET_TOKEN = "NexusAccess_Secret_2026";

    private final RestTemplate restTemplate = new RestTemplate();

    public String fetchAllLogs() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-nexus-secret", SECRET_TOKEN);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                AGENT_URL + "/all",
                HttpMethod.GET,
                entity,
                String.class
        );
        String resultado = this.callCleanup(3);
        System.out.println(resultado);

        return response.getBody();
    }
    public String callCleanup(int minutes) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-nexus-secret", SECRET_TOKEN);
        headers.setContentType(MediaType.APPLICATION_JSON); // Indispensable para enviar el body

        // 1. Creamos el cuerpo de la petición
        Map<String, Integer> body = new HashMap<>();
        body.put("maxAgeMinutes", minutes);

        // 2. Metemos el body Y los headers en el entity
        HttpEntity<Map<String, Integer>> entity = new HttpEntity<>(body, headers);
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    AGENT_URL + "/cleanup",
                    HttpMethod.DELETE,
                    entity,
                    String.class
            );

            return response.getBody();
        }catch (Exception e){
            return "CleanUp Error" + e.getMessage();
        }

    }
    public void sendRemoteLog(Map<String, Object> logData) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-nexus-secret", SECRET_TOKEN);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(logData, headers);

        restTemplate.postForEntity(AGENT_URL + "/receive", entity, String.class);
    }

}