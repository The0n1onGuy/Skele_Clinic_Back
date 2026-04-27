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

    @Transactional
    public SystemUsersModel createSystemUsers(SystemUsersRequestObject requestObject, SystemRolesModel assignedRole) {

        SystemUsersModel sysUser = new SystemUsersModel();

        // Asignación estricta al esquema maestro para usuarios de sistema
        sysUser.setTenantId("his_master");
        sysUser.setUserName(requestObject.getUserName());
        sysUser.setPassword(passwordEncoder.encode(requestObject.getPassword())); // Actualizado a 'getPassword()'
        sysUser.setRole(assignedRole);

        // Inicialización de seguridad TOTP (Por defecto desactivado hasta configuración manual)
        sysUser.setIs2faEnabled(false);
        sysUser.setTotpSecret(null);

        StatusModel activeStatus = statusRepository.findByStatusNameIgnoreCase("Active") // Homologado al Seeder
                .orElseThrow(() -> new IllegalStateException("Base status not found in the system."));
        sysUser.setStatus(activeStatus);

        return systemUsersrepository.save(sysUser);
    }
}