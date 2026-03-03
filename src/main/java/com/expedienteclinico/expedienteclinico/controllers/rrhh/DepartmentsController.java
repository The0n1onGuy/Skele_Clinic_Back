package com.expedienteclinico.expedienteclinico.controllers.rrhh;
import com.expedienteclinico.expedienteclinico.beans.rrhh.DepartmentObject;
import com.expedienteclinico.expedienteclinico.models.rrhh.DepartmentsModel;
import com.expedienteclinico.expedienteclinico.payload.response.ResponseFactory;
import com.expedienteclinico.expedienteclinico.services.rrhh.DepartmentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
//@RequestMapping("${RESTL}departments/")
// ESTA ES LA VERSION SIN EL V.E.
// @RequestMapping("/rrhh/departments/")
@RequestMapping("/api/rrhh/departments")
@CrossOrigin(origins = "*")
public class DepartmentsController {
    @Autowired
    DepartmentsService departmentService;

    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAll() {
        List<DepartmentObject> lista = departmentService.getAll();
        return new ResponseEntity<Map<String, Object>>( ResponseFactory.getSuccessOnGetAllResponse(lista) , HttpStatus.OK );

    }

    @PostMapping("/post")
    public ResponseEntity<Map<String, Object>> create(@RequestBody DepartmentObject deptoDto) {
        DepartmentObject nuevoDepto = departmentService.saveInfo(deptoDto);
        return new ResponseEntity<>(
                ResponseFactory.getCreatedResponse("Departamento creado con éxito", nuevoDepto),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody DepartmentObject deptoDto) {
        DepartmentObject actualizado = departmentService.updateInfo(id, deptoDto);

        if (actualizado == null) {
            return new ResponseEntity<>(
                    ResponseFactory.getNotFoundResponse(deptoDto),
                    HttpStatus.NOT_FOUND
            );
        }

        return new ResponseEntity<>(
                ResponseFactory.getUpdateResponse(actualizado),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        boolean deletedst = departmentService.deleteInfo(id);

        if (!deletedst) {
            return new ResponseEntity<>(
                    ResponseFactory.getNotFoundResponse(new DepartmentObject()),
                    HttpStatus.NOT_FOUND
            );
        }

        Map<String, Object> response = new java.util.HashMap<>();
        response.put("message", "Departamento desactivado correctamente.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
