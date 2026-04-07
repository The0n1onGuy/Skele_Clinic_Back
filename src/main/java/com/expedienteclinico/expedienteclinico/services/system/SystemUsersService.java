package com.expedienteclinico.expedienteclinico.services.system;

import com.expedienteclinico.expedienteclinico.beans.system.SystemUsersRequestObject;
import com.expedienteclinico.expedienteclinico.models.system.StatusModel;
import com.expedienteclinico.expedienteclinico.models.system.SystemRolesModel;
import com.expedienteclinico.expedienteclinico.models.system.SystemUsersModel;
import com.expedienteclinico.expedienteclinico.repositories.system.ISystemUsersRepository;
import com.expedienteclinico.expedienteclinico.repositories.system.IStatusRepository;
import com.expedienteclinico.expedienteclinico.repositories.system.ISystemRolesRepository;
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
        sysUser.setUserName(requestObject.getUserName());

        // Hashing obligatorio de la contraseña antes de persistir
        sysUser.setUserPassword(passwordEncoder.encode(requestObject.getUserPassword()));

        // Asignación de rol
        sysUser.setRole(assignedRole);

        // Resolución estricta del estatus activo
        StatusModel activeStatus = statusRepository.findById(1L)
                .orElseThrow(() -> new IllegalStateException("Estatus base no encontrado en el sistema."));
        sysUser.setStatus(activeStatus);

        return systemUsersrepository.save(sysUser);
    }
}