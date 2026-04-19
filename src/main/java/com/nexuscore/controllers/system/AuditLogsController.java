package com.nexuscore.controllers.system;

import com.nexuscore.services.audit.AuditLogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@Value("${RESTL}")
//private static String link;

@RestController
@RequestMapping("/rrhh/departments/")
@CrossOrigin(origins = "*")
public class AuditLogsController {
    @Autowired
    AuditLogsService logsService;

}
