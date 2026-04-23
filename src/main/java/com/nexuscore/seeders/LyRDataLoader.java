//package com.nexuscore.seeders;
//
//import com.nexuscore.models.cleaningandclothing.Cleaning_suppliesModel;
//import com.nexuscore.models.system.StatusModel;
//import com.nexuscore.repositories.cleaningandclothing.ICleaning_suppliesRepository;
//import com.nexuscore.repositories.system.IStatusRepository;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDate;
//
//
//@Component
//@Order(2) // DE MANERA ESTRICTA: Se ejecuta DESPUÉS de SystemDataLoader
//@Transactional
//public class LyRDataLoader implements CommandLineRunner {
//
//        private final ICleaning_suppliesRepository cleaningRepository;
//        private final IStatusRepository statusRepository;
//
//        @Value("${STATUS1:Active}")
//        private String statusName;
//
//        @Value("${SUPPLY_NAME:Cloro}")
//        private String supplyName;
//
//        @Value("${STOCK:50}")
//        private Integer stock;
//
//        @Value("${STOCK_MIN:20}")
//        private Integer stockMin;
//
//        @Value("${UNIT:ml}")
//        private String unit;
//
//        @Value("${EXP_DATE:2028-12-10}")
//        private String expDate;
//
//    public LyRDataLoader(ICleaning_suppliesRepository cleaningRepository,
//                         IStatusRepository statusRepository) {
//            this.cleaningRepository = cleaningRepository;
//            this.statusRepository = statusRepository;
//        }
//
//        @Override
//        public void run(String... args) {
//
//            StatusModel status = resolveStatus(statusName);
//
//            if (cleaningRepository.count() == 0) {
//                saveSupply(supplyName, unit, stock, stockMin, expDate, status);
//
//                System.out.println(">>> Cleaning Supplies Module: Seed data inserted successfully.");
//            }
//        }
//
//        private StatusModel resolveStatus(String name) {
//            return statusRepository.findAll().stream()
//                    .filter(s -> s.getStatusName().equalsIgnoreCase(name))
//                    .findFirst()
//                    .orElseThrow(() -> new RuntimeException(
//                            "CRITICAL ERROR: System status '" + name + "' not found. " +
//                                    "Ensure SystemDataLoader runs first."
//                    ));
//        }
//
//        private Cleaning_suppliesModel saveSupply(String name, String unit, Integer stock,
//                Integer stockMin, String expDate, StatusModel status) {
//
//            return cleaningRepository.findAll().stream()
//                    .filter(s -> s.getName().equalsIgnoreCase(name))
//                    .findFirst()
//                    .orElseGet(() -> {
//                        Cleaning_suppliesModel supply = new Cleaning_suppliesModel();
//                        supply.setName(name);
//                        supply.setUnitMeasurement(unit);
//                        supply.setCurrentStock(stock);
//                        supply.setStockMin(stockMin);
//                        supply.setExpirationDate(LocalDate.parse(expDate));
//                        supply.setStatus(status);
//
//                        return cleaningRepository.save(supply);
//                    });
//        }
//    }
