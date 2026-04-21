package com.nexuscore.services.system;

import com.nexuscore.beans.system.SystemUsersRequestObject;
import com.nexuscore.models.system.StatusModel;
import com.nexuscore.models.system.SystemRolesModel;
import com.nexuscore.models.system.SystemUsersModel;
import com.nexuscore.repositories.system.ISystemUsersRepository;
import com.nexuscore.repositories.system.IStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SystemUsersService {

    private final IStatusRepository statusRepository;
    private final ISystemUsersRepository systemUsersrepository;
    private final PasswordEncoder passwordEncoder; // Inyección crítica para seguridad

    @Transactional
    public SystemUsersModel createSystemUsers(SystemUsersRequestObject requestObject, SystemRolesModel assignedRole){

        SystemUsersModel sysUser = new SystemUsersModel();

        sysUser.setTenantId("his_master");

        sysUser.setUserName(requestObject.getUserName());

        // Hashing obligatorio de la contraseña antes de persistir
        sysUser.setUserPassword(passwordEncoder.encode(requestObject.getUserPassword()));

        // Asignación de rol
        sysUser.setRole(assignedRole);

        // Resolución estricta del estatus activo
        StatusModel activeStatus = statusRepository.findByStatusNameIgnoreCase("Active")
                .orElseThrow(() -> new IllegalStateException("Estatus base no encontrado en el sistema."));
        sysUser.setStatus(activeStatus);

        return systemUsersrepository.save(sysUser);
    }
}