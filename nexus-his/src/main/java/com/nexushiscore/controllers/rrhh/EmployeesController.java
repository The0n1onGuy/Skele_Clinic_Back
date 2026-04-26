package com.nexushiscore.controllers.rrhh;

import com.nexushiscore.beans.rrhh.EmployeeOnboardingRequestObject;
import com.nexushiscore.beans.rrhh.EmployeesObject;
import com.nexussharedcore.payload.response.ResponseFactory;
import com.nexussharedcore.security.JwtService;
import com.nexushiscore.services.rrhh.EmployeeOnboardingService;
import com.nexushiscore.services.rrhh.EmployeesService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
// @CrossOrigin(origins = "*")
@RequestMapping("/api/his/v1/rrhh/employees")
public class EmployeesController {
    @Autowired
    EmployeesService employeesService;
    @Autowired
    JwtService jwtService;
    @Autowired
    private EmployeeOnboardingService onboardingService;
    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAll() {
        List<EmployeesObject> employees_list = employeesService.getAll();
        return new ResponseEntity<Map<String, Object>>( ResponseFactory.getSuccessOnGetAllResponse(employees_list) , HttpStatus.OK );

    }

    @PostMapping("/create")
    public ResponseEntity<?> onboardEmployee(
            @Valid @RequestBody EmployeeOnboardingRequestObject request,
            HttpServletRequest httpRequest) {

        // 1. Extracción Estricta (Zero Trust)
        String authHeader = httpRequest.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token ausente o inválido");
        }

        String token = authHeader.substring(7);
        String currentTenantId = jwtService.extractTenant(token);

        // 2. Ejecución de la Transacción Distribuida
        onboardingService.onboardNewEmployee(request, currentTenantId);

        // 3. (Opcional) Si tu ResponseFactory ya funciona, utilízalo aquí.
        return ResponseEntity.status(HttpStatus.CREATED).body("Empleado registrado exitosamente en el sistema y en la clínica.");
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
