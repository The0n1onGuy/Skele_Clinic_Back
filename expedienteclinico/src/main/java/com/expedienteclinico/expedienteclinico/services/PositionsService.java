package com.expedienteclinico.expedienteclinico.services;
import com.expedienteclinico.expedienteclinico.models.Positions;
import com.expedienteclinico.expedienteclinico.repositories.IPositionsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PositionsService {
    @Autowired
    IPositionsRepo positionsRepo;

    public List<Positions> getPos(){
        return positionsRepo.findAll();
    }

}
