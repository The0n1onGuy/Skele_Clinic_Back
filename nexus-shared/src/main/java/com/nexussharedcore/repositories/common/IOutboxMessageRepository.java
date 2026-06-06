package com.nexussharedcore.repositories.common;

import com.nexussharedcore.models.common.OutboxMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Interfaz de repositorio para la persistencia y lectura de mensajes del Outbox.
 * Administrada a nivel común en nexus-shared.
 */
@Repository
public interface IOutboxMessageRepository extends JpaRepository<OutboxMessage, String> {
    
    /**
     * Recupera todos los mensajes pendientes de despacho hacia el middleware.
     *
     * @param status Estado del mensaje (usualmente 'PENDING')
     * @return Lista de mensajes pendientes de envío
     */
    List<OutboxMessage> findByStatus(String status);
}
