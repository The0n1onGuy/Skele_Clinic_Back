package com.expedienteclinico.expedienteclinico.seeders;

import com.expedienteclinico.expedienteclinico.models.StatusModel;
import com.expedienteclinico.expedienteclinico.repositories.IStatusRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Component
@Order(1)
public class SystemDataLoader implements CommandLineRunner {

    private final IStatusRepository statusRepository;

    // Corrección de nomenclatura a camelCase
    @Value("${STATUS1:Active}")
    private String activeStatus;

    @Value("${STATUS2:Inactive}")
    private String inactiveStatus;

    @Value("${STATUS3:Edited}")
    private String editedStatus;

    @Value("${STATUS4:Deleted}")
    private String deletedStatus;

    @Value("${STATUS5:Added}")
    private String addedStatus;

    public SystemDataLoader(IStatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        // Agrupamos para iterar y aplicar la regla de idempotencia limpiamente
        List<String> statusesToSeed = Arrays.asList(
                activeStatus, inactiveStatus, editedStatus, deletedStatus, addedStatus
        );

        for (String statusName : statusesToSeed) {
            resolveStatus(statusName);
        }

        System.out.println(">>> Módulo Sistema: Catálogo base de Estatus sembrado con éxito.");
    }

    /**
     * Lógica estricta de "Buscar o Crear" (Idempotencia).
     * Evita colisiones de llaves únicas sin importar cuántas veces se reinicie la API.
     */
    private void resolveStatus(String name) {
        statusRepository.findAll().stream()
                .filter(s -> s.getStatusName().equalsIgnoreCase(name))
                .findFirst()
                .orElseGet(() -> {
                    StatusModel nuevo = new StatusModel();
                    nuevo.setStatusName(name);
                    // El UUID se delega a la inicialización de la entidad: java.util.UUID.randomUUID().toString()
                    return statusRepository.save(nuevo);
                });
    }
}