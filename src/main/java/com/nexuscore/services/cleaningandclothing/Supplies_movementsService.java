package com.nexuscore.services.cleaningandclothing;


import com.nexuscore.beans.cleaningandclothing.Supplies_movementsObject;
import com.nexuscore.models.cleaningandclothing.Supplies_movementsModel;
import com.nexuscore.repositories.cleaningandclothing.ISupplies_movementsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class Supplies_movementsService {

    @Autowired
    private ISupplies_movementsRepository supplies_movementsRepository;

    public List<Supplies_movementsObject> getAll() {


        List<Supplies_movementsModel> suppliesM = supplies_movementsRepository.findAll();
        List<Supplies_movementsObject> dts = new ArrayList<>();

        for (Supplies_movementsModel supplieM : suppliesM) {

            Supplies_movementsObject dtos = new Supplies_movementsObject();

            dtos.setId_motion(supplieM.getId_motion());
            dtos.setMovementType(supplieM.getMovementType());
            dtos.setAmount(supplieM.getAmount());
            dtos.setMovementDate(String.valueOf(supplieM.getMovementDate()));
            dtos.setObservations(supplieM.getObservations());
            if (supplieM.getSupply() != null) {
                dtos.setSupply(supplieM.getSupply().getSupplies_id());
            }
            if (supplieM.getEmployee() != null) {
                dtos.setEmployeesName(supplieM.getEmployee().getNombre());
            }

        }
        return dts;
    }
}
