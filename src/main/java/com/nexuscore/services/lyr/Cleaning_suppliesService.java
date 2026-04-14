package com.nexuscore.services.lyr;



import com.nexuscore.beans.lyr.Cleaning_suppliesObject;
import com.nexuscore.models.lyr.Cleaning_suppliesModel;
import com.nexuscore.repositories.lyr.ICleaning_suppliesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class Cleaning_suppliesService {

    @Autowired
    private ICleaning_suppliesRepository iCleaning_suppliesRepository;

    public List<com.nexuscore.beans.lyr.Cleaning_suppliesObject> getAll() {


        List<Cleaning_suppliesModel> supplies = iCleaning_suppliesRepository.findAll() ;
        List<Cleaning_suppliesObject> dts = new ArrayList<>();

        for (Cleaning_suppliesModel supplie : supplies) {

            Cleaning_suppliesObject dtos = new Cleaning_suppliesObject();

            dtos.setId_supplies(supplie.getId_supplies());
            dtos.setName(supplie.getName());
            dtos.setUnit_measurement(supplie.getUnit_measurement());
            dtos.setCurrent_stock(supplie.getCurrent_stock());
            dtos.setStock_min(supplie.getStock_min());
            dtos.setExpiration_date(supplie.getExpiration_date());
           if (supplie.getStatus() != null) {
                dtos.setStatusId(supplie.getStatus().getId());
                dtos.setStatusNombre(supplie.getStatus().getStatusName());
            }
            dts.add(dtos);
        }
        return dts;
    }
}
