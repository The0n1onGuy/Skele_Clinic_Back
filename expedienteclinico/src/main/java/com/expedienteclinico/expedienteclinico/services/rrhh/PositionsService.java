package com.expedienteclinico.expedienteclinico.services.rrhh;
import com.expedienteclinico.expedienteclinico.models.rrhh.PositionsModel;
import com.expedienteclinico.expedienteclinico.repositories.rrhh.IPositionsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PositionsService {
    @Autowired
    IPositionsRepo positionsRepo;

    public List<PositionsModel> getPos(){
        return positionsRepo.findAll();
    }

}
