package com.expedienteclinico.expedienteclinico.controllers.rrhh;

import com.expedienteclinico.expedienteclinico.beans.rrhh.EmployeesObject;
import com.expedienteclinico.expedienteclinico.beans.rrhh.EmployeesObject;
import com.expedienteclinico.expedienteclinico.payload.response.ResponseFactory;
import com.expedienteclinico.expedienteclinico.services.rrhh.EmployeesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/rrhh/employees")
public class EmployeesController {
    @Autowired
    EmployeesService employeesService;

    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAll() {
        List<EmployeesObject> employees_list = employeesService.getAll();
        return new ResponseEntity<Map<String, Object>>( ResponseFactory.getSuccessOnGetAllResponse(employees_list) , HttpStatus.OK );

    }

    @PostMapping("/post")
    public ResponseEntity<Map<String, Object>> create(@RequestBody EmployeesObject emploDto) {
        EmployeesObject newEmployee = employeesService.saveInfo(emploDto);
        return new ResponseEntity<>(
                ResponseFactory.getCreatedResponse("Empleado registrado con éxito", newEmployee),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody EmployeesObject emploDto) {
        EmployeesObject updatedemployee = employeesService.updateInfo(id, emploDto);

        if (updatedemployee == null) {
            return new ResponseEntity<>(
                    ResponseFactory.getNotFoundResponse(emploDto),
                    HttpStatus.NOT_FOUND
            );
        }

        return new ResponseEntity<>(
                ResponseFactory.getUpdateResponse(updatedemployee),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        boolean deletedst = employeesService.deleteInfo(id);

        if (!deletedst) {
            return new ResponseEntity<>(
                    ResponseFactory.getNotFoundResponse(new EmployeesObject()),
                    HttpStatus.NOT_FOUND
            );
        }

        Map<String, Object> response = new java.util.HashMap<>();
        response.put("message", "Empleado desactivado correctamente.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
