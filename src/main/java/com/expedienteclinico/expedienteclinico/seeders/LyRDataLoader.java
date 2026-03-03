package com.expedienteclinico.expedienteclinico.seeders;

import com.expedienteclinico.expedienteclinico.models.StatusModel;
import com.expedienteclinico.expedienteclinico.models.lyr.Cleaning_suppliesModel;
import com.expedienteclinico.expedienteclinico.repositories.IStatusRepository;
import com.expedienteclinico.expedienteclinico.repositories.lyr.ICleaning_suppliesRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2) // DE MANERA ESTRICTA: Se ejecuta DESPUÉS de SystemDataLoader
public class LyRDataLoader implements CommandLineRunner {

    private final ICleaning_suppliesRepository cleaningRepository;
    private final IStatusRepository statusRepository;

    @Value("${STATUS1:Active}")
    private String statusName;

    @Value("${ARTICULO1:Cloro}")
    private String articuloName;

    @Value("${STOCK:50}")
    private Integer stock;

    @Value("${STOCK_MIN:20}")
    private Integer stock_min;

    @Value("${UNIT:ml}")
    private String unit;

    @Value("${EXP_DATE:10/12/2028}")
    private String exp_date;

    public LyRDataLoader(ICleaning_suppliesRepository cleaningRepository,
                         IStatusRepository statusRepository) {
        this.cleaningRepository = cleaningRepository;
        this.statusRepository = statusRepository;
    }

    @Override
    public void run(String... args) {
        // Delegamos la validación del estado al método de resolución estricto
        StatusModel statusA = resolveStatus(statusName);

        if (cleaningRepository.count() == 0) {
            Cleaning_suppliesModel supplies = new Cleaning_suppliesModel();
            supplies.setName(articuloName);
            supplies.setCurrent_stock(stock);
            supplies.setStock_min(stock_min);
            supplies.setUnit_measurement(unit);
            supplies.setExpiration_date(exp_date);
            supplies.setStatus(statusA);

            cleaningRepository.save(supplies);
            System.out.println(">>> Módulo LyR: Datos insertados correctamente");
        }
    }

    /**
     * Búsqueda estricta para evitar la creación de duplicados.
     */
    private StatusModel resolveStatus(String name) {
        return statusRepository.findAll().stream()
                .filter(s -> s.getStatusName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(
                        "ERROR CRÍTICO: El estado de sistema '" + name + "' no ha sido precargado. " +
                                "Asegúrese de que SystemDataLoader se ejecute primero."
                ));
    }
}