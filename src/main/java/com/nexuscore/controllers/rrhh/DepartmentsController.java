package com.nexuscore.controllers.rrhh;
import com.nexuscore.beans.rrhh.DepartmentObject;
import com.nexuscore.payload.response.ResponseFactory;
import com.nexuscore.services.rrhh.DepartmentsService;
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
@RequestMapping("/api/his/v1/rrhh/departments")
// @CrossOrigin(origins = "*")
public class DepartmentsController {
    @Autowired
    DepartmentsService departmentService;

    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAll() {
        List<DepartmentObject> dep_list = departmentService.getAll();
        return new ResponseEntity<Map<String, Object>>( ResponseFactory.getSuccessOnGetAllResponse(dep_list) , HttpStatus.OK );

    }

    @PostMapping("/post")
    public ResponseEntity<Map<String, Object>> create(@RequestBody DepartmentObject objectDto) {
        DepartmentObject newObject = departmentService.saveInfo(objectDto);
        return new ResponseEntity<>(
                ResponseFactory.getCreatedResponse("Departamento creado con éxito", newObject),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody DepartmentObject objectDto) {
        DepartmentObject updatedObject = departmentService.updateInfo(id, objectDto);
        if (updatedObject == null) {
            return new ResponseEntity<>(
                    ResponseFactory.getNotFoundResponse(objectDto),
                    HttpStatus.NOT_FOUND
            );
        }

        return new ResponseEntity<>(
                ResponseFactory.getUpdateResponse(updatedObject),
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
