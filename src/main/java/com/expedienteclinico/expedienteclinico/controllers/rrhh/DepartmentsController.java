package com.expedienteclinico.expedienteclinico.controllers.rrhh;
import com.expedienteclinico.expedienteclinico.models.rrhh.DepartmentsModel;
import com.expedienteclinico.expedienteclinico.services.rrhh.DepartmentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
//@RequestMapping("${RESTL}departments/")
// ESTA ES LA VERSION SIN EL V.E.
// @RequestMapping("/rrhh/departments/")
@RequestMapping("/api/rrhh/departments/")
@CrossOrigin(origins = "*")
public class DepartmentsController {
    @Autowired
    DepartmentsService departmentService;

    @GetMapping("all")
    public List<DepartmentsModel> getAll() {
        return departmentService.getAll();
    }

    @PostMapping("post")
    public DepartmentsModel create(@RequestBody DepartmentsModel depto) {
        return departmentService.saveInfo(depto);
    }

    @PutMapping("update/{id}")
    public DepartmentsModel update(@PathVariable Long id, @RequestBody DepartmentsModel depto) {
        return departmentService.updateInfo(id, depto);
    }

    @DeleteMapping("delete/{id}")
    public String delete(@PathVariable Long id) {
        boolean ok = departmentService.deleteInfo(id);
        return ok ? "Eliminado correctamente" : "No se pudo eliminar";
    }

}
