package com.nexushiscore.services.cleaningandclothing;

import com.nexushiscore.beans.cleaningandclothing.Cleaning_suppliesObject;
import com.nexushiscore.models.cleaningandclothing.Cleaning_suppliesModel;
import com.nexushiscore.repositories.cleaningandclothing.ICleaning_suppliesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class Cleaning_suppliesService {

    private final ICleaning_suppliesRepository repository;

    public List<Cleaning_suppliesObject> getAll() {

        List<Cleaning_suppliesModel> entities = repository.findAll();
        List<Cleaning_suppliesObject> dtos = new ArrayList<>();

        for (Cleaning_suppliesModel entity : entities) {

            Cleaning_suppliesObject dto = new Cleaning_suppliesObject();

            // mapping directo
            dto.setUuid(entity.getUuid());
            dto.setSuppliesId(entity.getSupplies_id());
            dto.setName(entity.getName());
            dto.setUnitMeasurement(entity.getUnitMeasurement());
            dto.setCurrentStock(entity.getCurrentStock());
            dto.setStockMin(entity.getStockMin());
            dto.setExpirationDate(entity.getExpirationDate());

            // mapping relacional
            if (entity.getStatus() != null) {
                dto.setStatusUuid(entity.getStatus().getUuid());
                dto.setStatusName(entity.getStatus().getStatusName());
            }

            dtos.add(dto);
        }

        return dtos;
    }
}