package com.nexuscoreserver.services.system;

import com.nexuscoreserver.beans.system.SystemUsersRequestObject;
import com.nexuscoreserver.beans.system.EmployeeOnboardRequestObject;
import com.nexuscoreserver.models.system.StatusModel;
import com.nexuscoreserver.models.system.SystemRolesModel;
import com.nexuscoreserver.models.system.SystemUsersModel;
import com.nexuscoreserver.repositories.system.ISystemUsersRepository;
import com.nexuscoreserver.repositories.system.ISystemRolesRepository;
import com.nexuscoreserver.repositories.system.IStatusRepository;
import com.nexussharedcore.repositories.common.IOutboxMessageRepository;
import com.nexussharedcore.models.common.OutboxMessage;
import com.nexussharedcore.events.UserOnboardRequestedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio de negocio para la gestión e identidades de usuarios de sistema en CORE.
 * Robustecido para soportar orquestación SAGA asíncrona mediante el motor central de Outbox.
 */
@Service
@RequiredArgsConstructor
public class SystemUsersService {

    private final IStatusRepository statusRepository;
    private final ISystemUsersRepository systemUsersrepository;
    private final ISystemRolesRepository systemRolesRepository;
    private final IOutboxMessageRepository outboxRepository;
    private final PasswordEncoder passwordEncoder;

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

    /**
     * Orquestador transaccional SAGA para el alta de empleados (Onboarding).
     * Crea de manera atómica las credenciales de sistema locales y almacena el evento en la tabla Outbox
     * para su despacho asíncrono y desacoplado, evitando fallos de split-brain clínico.
     */
    @Transactional
    public void onboardEmployee(EmployeeOnboardRequestObject request) {
        String eventId = java.util.UUID.randomUUID().toString();
        
        // 1. Si requiere acceso al sistema, creamos las credenciales de forma transaccional local
        if (request.getRequiresSystemAccess() != null && request.getRequiresSystemAccess()) {
            if (request.getUsername() == null || request.getPassword() == null || request.getRoleName() == null) {
                throw new IllegalArgumentException("Usuario, contraseña y rol son obligatorios si requiere acceso al sistema.");
            }
            
            // Validar que el usuario no exista previamente para evitar colisiones
            if (systemUsersrepository.findByUserName(request.getUsername()).isPresent()) {
                throw new RuntimeException("El nombre de usuario '" + request.getUsername() + "' ya está registrado en el sistema.");
            }

            SystemUsersModel sysUser = new SystemUsersModel();
            sysUser.setUuid(eventId); // Vinculación atómica de la credencial con el EventId de la SAGA para correlación y aislamiento
            sysUser.setTenantId(request.getTenantId());
            sysUser.setUserName(request.getUsername());
            sysUser.setPassword(passwordEncoder.encode(request.getPassword()));

            SystemRolesModel assignedRole = systemRolesRepository.findByRoleNameIgnoreCase(request.getRoleName())
                    .orElseThrow(() -> new RuntimeException("El rol '" + request.getRoleName() + "' no existe en el sistema."));
            sysUser.setRole(assignedRole);
            sysUser.setIs2faEnabled(false);
            sysUser.setTotpSecret(null);

            StatusModel activeStatus = statusRepository.findByStatusNameIgnoreCase("Active")
                    .orElseThrow(() -> new IllegalStateException("Base status not found in the system."));
            sysUser.setStatus(activeStatus);

            systemUsersrepository.save(sysUser);
        }

        // 2. Registro Transaccional en el Outbox Maestre (Paso 1 de la SAGA)
        try {
            UserOnboardRequestedEvent event = new UserOnboardRequestedEvent(
                    eventId,
                    request.getTenantId(),
                    request.getRequiresSystemAccess(),
                    request.getUsername(),
                    request.getPassword(),
                    request.getRoleName(),
                    request.getName(),
                    request.getPatname(),
                    request.getMatname(),
                    request.getCurp(),
                    request.getRfc(),
                    request.getGender(),
                    request.getDatebirth(),
                    request.getIdDepartment(),
                    request.getIdPosition()
            );

            // Instancia local de ObjectMapper para evitar inyecciones fallidas bajo Jackson 3
            ObjectMapper mapper = new ObjectMapper();
            String jsonPayload = mapper.writeValueAsString(event);

            // Ruteamos el alta del empleado al módulo correspondiente (his o pos)
            String routingKey = "user.onboard." + (request.getTenantId().startsWith("pos") ? "pos" : "his");

            OutboxMessage outboxMessage = OutboxMessage.builder()
                    .id(eventId)
                    .aggregateType("EMPLOYEE")
                    .aggregateId(request.getCurp())
                    .eventType("UserOnboardRequestedEvent")
                    .payload(jsonPayload)
                    .routingKey(routingKey)
                    .status("PENDING")
                    .createdAt(java.time.LocalDateTime.now())
                    .build();

            outboxRepository.save(outboxMessage);

        } catch (Exception e) {
            throw new RuntimeException("Error al registrar de forma atómica el Outbox de alta de empleado.", e);
        }
    }
}