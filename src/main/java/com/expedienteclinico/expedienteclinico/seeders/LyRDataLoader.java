package com.expedienteclinico.expedienteclinico.seeders;

import com.expedienteclinico.expedienteclinico.models.StatusModel;
import com.expedienteclinico.expedienteclinico.models.lyr.Cleaning_suppliesModel;
import com.expedienteclinico.expedienteclinico.repositories.IStatusRepository;
import com.expedienteclinico.expedienteclinico.repositories.lyr.ICleaning_suppliesRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class LyRDataLoader implements CommandLineRunner {

    private final ICleaning_suppliesRepository cleaningRepository;
    private final IStatusRepository statusRepository;

    @Value("${STATUS1}")
    private String statusName;

    @Value("${STATUS2}")
    private String StatusName2;

    @Value("${ARTICULO1}")
    private String articuloName;

    @Value("${STOCK}")
    private Integer stock;

    @Value("${STOCK_MIN}")
    private Integer stock_min;

    @Value("${UNIT}")
    private String unit;

    @Value("${EXP_DATE}")
    private String exp_date;

    public LyRDataLoader(ICleaning_suppliesRepository cleaningRepository,
                         IStatusRepository statusRepository) {

        this.cleaningRepository = cleaningRepository;
        this.statusRepository = statusRepository;
    }

    @Override
    public void run(String... args) {

        if (statusRepository.count() == 0) {
            StatusModel status = new StatusModel();
            status.setStatusName(statusName);
            statusRepository.save(status);
        }

        if (statusRepository.count() == 1) {
            StatusModel status2 = new StatusModel();
            status2.setStatusName(StatusName2);
            statusRepository.save(status2);
        }

        StatusModel statusA = statusRepository
                .findById(1L)
                .orElseThrow(() -> new RuntimeException("Status no encontrado"));

        if (cleaningRepository.count() == 0) {

            Cleaning_suppliesModel supplies = new Cleaning_suppliesModel();

            supplies.setName(articuloName);
            supplies.setCurrent_stock(stock);
            supplies.setStock_min(stock_min);
            supplies.setUnit_measurement(unit);
            supplies.setExpiration_date(exp_date);
            supplies.setStatus(statusA);

            cleaningRepository.save(supplies);
        }

        System.out.println("Datos insertados correctamente");
    }
}


//SOLIO BIEN LA EJECUCION AWEBO

