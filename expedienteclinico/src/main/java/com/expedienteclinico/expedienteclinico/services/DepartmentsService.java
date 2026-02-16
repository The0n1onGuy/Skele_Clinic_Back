package com.expedienteclinico.expedienteclinico.services;
import com.expedienteclinico.expedienteclinico.models.Departments;
import com.expedienteclinico.expedienteclinico.repositories.IDepartmentsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class DepartmentsService {
    @Autowired
    IDepartmentsRepo departmentsRepo;

    public List<Departments> getAll() {
        return departmentsRepo.findAll();
    }

    public Departments saveInfo(Departments depto) {
        return departmentsRepo.save(depto);
    }
}
