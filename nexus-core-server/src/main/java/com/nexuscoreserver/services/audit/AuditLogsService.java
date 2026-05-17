package com.nexuscoreserver.services.audit;

import com.nexuscoreserver.models.system.AuditLogsModel;
import com.nexuscoreserver.models.system.StatusModel;
import com.nexuscoreserver.repositories.system.IAuditLogsRepository;
import com.nexuscoreserver.repositories.system.IStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class AuditLogsService {
    @Autowired
    IAuditLogsRepository auditLogsRepository;

    @Autowired
    IStatusRepository statusRepo;

    private String defaultUser = "Usuario NO encontrado";

    public void logAction(String concept, String nameStatus) {
        StatusModel status = statusRepo.findByStatusNameIgnoreCase(nameStatus)
                .orElseThrow(() -> new RuntimeException("Estado no encontrado: " + nameStatus));

        //OBTEN CULPABLE POR TOKEN
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username;
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            username = auth.getName(); // El usuario que obtuvimos
        } else {
            username = defaultUser; // Si no lo encontramos por lo menos registra que se modifico y es hora de Sherlock
        }

        AuditLogsModel log = new AuditLogsModel();
        log.setConcept_audit(concept);
        log.setUser_blamed(username);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        log.setDate_ocurrence(LocalDateTime.now().format(dtf));

        log.setStatus(status);

        auditLogsRepository.save(log);
    }



}
