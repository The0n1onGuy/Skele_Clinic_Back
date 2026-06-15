package com.nexuscoreserver.listeners.system;

import com.nexussharedcore.events.UserOnboardFailedEvent;
import com.nexuscoreserver.repositories.system.ISystemUsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Componente Listener para la orquestación SAGA en el Core Server.
 * Consume eventos de fallo desde RabbitMQ y ejecuta la transacción de compensación
 * (Rollback lógico/físico de credenciales de usuario creadas en base maestra) para
 * garantizar la consistencia eventual ante caídas de persistencia local en los inquilinos.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CoreUserOnboardEventListener {

    private final ISystemUsersRepository systemUsersRepository;

    /**
     * Intercepta fallos de registro de expedientes clínicos en inquilinos.
     * Realiza un rollback de credenciales en la base de datos central de forma atómica.
     */
    @RabbitListener(queues = "core.user.onboard.failed.queue")
    @Transactional
    public void onOnboardFailed(UserOnboardFailedEvent event) {
        log.warn("<<< [SAGA COMPENSACIÓN] Fallo de Onboarding recibido. Revirtiendo usuario '{}' en tenant '{}'. Causa: {}",
                event.getUsername(), event.getTenantId(), event.getErrorMessage());

        // Buscamos el usuario en base maestra
        systemUsersRepository.findByUserName(event.getUsername()).ifPresentOrElse(user -> {
            // Se valida atómicamente que el UUID del usuario coincida con el eventId de la transacción fallida.
            // Esto evita que un reintento manual tardío o duplicado borre las credenciales originales activas.
            if (user.getUuid().equalsIgnoreCase(event.getEventId())) {
                systemUsersRepository.delete(user);
                log.info("<<< [SAGA COMPENSACIÓN COMPLETADA] El usuario '{}' con UUID '{}' ha sido eliminado de la base central.", event.getUsername(), event.getEventId());
            } else {
                log.warn("<<< [SAGA COMPENSACIÓN OMITIDA] El usuario '{}' existe pero su UUID '{}' no coincide con el EventId de fallo '{}'. Operación de rollback descartada.", 
                        event.getUsername(), user.getUuid(), event.getEventId());
            }
        }, () -> {
            log.info("<<< [SAGA COMPENSACIÓN OMITIDA] El usuario '{}' no existe en base maestra. Ignorando.", event.getUsername());
        });
    }
}
