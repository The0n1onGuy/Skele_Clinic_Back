package com.nexuscore.controllers.rrhh;

import com.nexuscore.beans.rrhh.ContractObject;
import com.nexuscore.payload.response.ResponseFactory;
import com.nexuscore.services.rrhh.ContractsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
// @CrossOrigin(origins = "*")
@RequestMapping("/api/his/v1/rrhh/contract")
public class ContractsController {
    @Autowired
    ContractsService contractsService;

    @GetMapping("/all")
    public List<ContractObject> getAll(){
        return contractsService.getAll();
    }

    @PostMapping("/post")
    public ResponseEntity<Map<String, Object>> create(@RequestBody ContractObject objectDto) {
        ContractObject newObject = contractsService.saveInfo(objectDto);
        return new ResponseEntity<>(
                ResponseFactory.getCreatedResponse("Contracto registrado exitosamente", newObject),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody ContractObject objectDto) {
        ContractObject updatedObject = contractsService.updateInfo(id, objectDto);
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
        boolean deletedst = contractsService.deleteInfo(id);

        if (!deletedst) {
            return new ResponseEntity<>(
                    ResponseFactory.getNotFoundResponse(new ContractObject()),
                    HttpStatus.NOT_FOUND
            );
        }

        Map<String, Object> response = new java.util.HashMap<>();
        response.put("message", "Contrato desactivado.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
