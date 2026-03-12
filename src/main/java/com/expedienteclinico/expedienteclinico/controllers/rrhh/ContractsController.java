package com.expedienteclinico.expedienteclinico.controllers.rrhh;

import com.expedienteclinico.expedienteclinico.beans.rrhh.ContractObject;
import com.expedienteclinico.expedienteclinico.models.rrhh.ContractsModel;
import com.expedienteclinico.expedienteclinico.services.rrhh.ContractsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/rrhh/contract/")
public class ContractsController {
    @Autowired
    ContractsService contractsService;

    @GetMapping("get")
    public List<ContractObject> getAll(){
        return contractsService.getAll();
    }

}
