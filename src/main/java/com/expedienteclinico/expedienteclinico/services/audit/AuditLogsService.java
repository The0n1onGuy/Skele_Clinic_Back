package com.expedienteclinico.expedienteclinico.services.audit;

import com.expedienteclinico.expedienteclinico.models.audit.AuditLogsModel;
import com.expedienteclinico.expedienteclinico.models.system.StatusModel;
import com.expedienteclinico.expedienteclinico.repositories.audit.IAuditLogsRepository;
import com.expedienteclinico.expedienteclinico.repositories.system.IStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class AuditLogsService {
    @Autowired
    IAuditLogsRepository auditLogsRepository;

    @Autowired
    IStatusRepository statusRepo;

    @Value("${DBUSER:Olan}")
    private String dbUser;

    public void registrarAccion(String concept, String nameStatus) {
        StatusModel status = statusRepo.findByStatusNameIgnoreCase(nameStatus)
                .orElseThrow(() -> new RuntimeException("Estado no encontrado: " + nameStatus));

        AuditLogsModel log = new AuditLogsModel();
        log.setConcept_audit(concept);
        log.setUser_blamed(dbUser);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        log.setDate_ocurrence(LocalDateTime.now().format(dtf));

        log.setStatus(status);

        auditLogsRepository.save(log);
    }



}
