package com.nexuscoreserver.services.system;

import com.nexuscoreserver.beans.system.SystemUsersRequestObject;
import com.nexuscoreserver.models.system.StatusModel;
import com.nexuscoreserver.models.system.SystemRolesModel;
import com.nexuscoreserver.models.system.SystemUsersModel;
import com.nexuscoreserver.repositories.system.ISystemUsersRepository;
import com.nexuscoreserver.repositories.system.IStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SystemUsersService {

    private final IStatusRepository statusRepository;
    private final ISystemUsersRepository systemUsersrepository;
    private final PasswordEncoder passwordEncoder;

    // En SystemUsersService.java
    @Transactional
    public SystemUsersModel createSystemUsers(SystemUsersRequestObject requestObject, SystemRolesModel assignedRole, String tenantId) {

        SystemUsersModel sysUser = new SystemUsersModel();

        // Ahora el tenant es dinámico: puede ser "his_master" o el ID de una clínica específica
        sysUser.setTenantId(tenantId);
        sysUser.setUserName(requestObject.getUserName());
        sysUser.setPassword(passwordEncoder.encode(requestObject.getPassword()));
        sysUser.setRole(assignedRole);

        // El estado "requiere configuración" es implícito: 2fa=false y secret=null
        sysUser.setIs2faEnabled(false);
        sysUser.setTotpSecret(null);

        StatusModel activeStatus = statusRepository.findByStatusNameIgnoreCase("Active")
                .orElseThrow(() -> new IllegalStateException("Base status not found in the system."));
        sysUser.setStatus(activeStatus);

        return systemUsersrepository.save(sysUser);
    }
}