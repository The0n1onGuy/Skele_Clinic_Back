package com.expedienteclinico.expedienteclinico.controllers;
import com.expedienteclinico.expedienteclinico.models.Departments;
import com.expedienteclinico.expedienteclinico.services.DepartmentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/rh/departments/")
@CrossOrigin(origins = "*")
public class DepartmentsController {
    @Autowired
    DepartmentsService departmentService;

    @GetMapping("get")
    public List<Departments> getAll() {
        return departmentService.getAll();
    }
}
