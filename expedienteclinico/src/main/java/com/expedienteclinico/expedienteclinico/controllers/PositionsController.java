package com.expedienteclinico.expedienteclinico.controllers;

import com.expedienteclinico.expedienteclinico.models.Positions;
import com.expedienteclinico.expedienteclinico.services.PositionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/rh/positions/")
public class PositionsController {
    @Autowired
    PositionsService positionsService;
    @GetMapping("get")
    public List<Positions> getPos(){
        return positionsService.getPos();
    }

}
