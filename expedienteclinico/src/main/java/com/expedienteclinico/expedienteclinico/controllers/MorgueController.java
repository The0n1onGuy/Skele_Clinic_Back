package com.expedienteclinico.expedienteclinico.controllers;

import com.expedienteclinico.expedienteclinico.beans.MorgueObject;
import com.expedienteclinico.expedienteclinico.models.Morgue;
import com.expedienteclinico.expedienteclinico.services.MorgueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/morgue-controller/")
public class MorgueController {

    @Autowired
    MorgueService morgueService;

    @GetMapping("morgue-list")
    public ArrayList<Object> getMorgueList() {
        return morgueService.getMorgueList();
    }

    @GetMapping("all")
    public List<Morgue> getAll() {
        return morgueService.getAll();
    }
}