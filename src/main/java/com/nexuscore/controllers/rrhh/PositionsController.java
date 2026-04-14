package com.nexuscore.controllers.rrhh;

import com.nexuscore.beans.rrhh.PositionsObject;
import com.nexuscore.payload.response.ResponseFactory;
import com.nexuscore.services.rrhh.PositionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
// @CrossOrigin(origins = "*")
@RequestMapping("/api/his/v1/rrhh/positions")
public class PositionsController {
    @Autowired
    PositionsService positionsService;

    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAll() {
        List<PositionsObject> post_list = positionsService.getAll();
        return new ResponseEntity<Map<String, Object>>( ResponseFactory.getSuccessOnGetAllResponse(post_list) , HttpStatus.OK );
    }

    @PostMapping("/post")
    public ResponseEntity<Map<String, Object>> create(@RequestBody PositionsObject positionDto) {
        PositionsObject newPosition = positionsService.saveInfo(positionDto);
        return new ResponseEntity<>(
                ResponseFactory.getCreatedResponse("Posicion nueva creada", newPosition),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody PositionsObject positionDto) {
        PositionsObject updated = positionsService.updateInfo(id, positionDto);

        if (updated == null) {
            return new ResponseEntity<>(
                    ResponseFactory.getNotFoundResponse(positionDto),
                    HttpStatus.NOT_FOUND
            );
        }

        return new ResponseEntity<>(
                ResponseFactory.getUpdateResponse(updated),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        boolean deletedst = positionsService.deleteInfo(id);

        if (!deletedst) {
            return new ResponseEntity<>(
                    ResponseFactory.getNotFoundResponse(new PositionsObject()),
                    HttpStatus.NOT_FOUND
            );
        }

        Map<String, Object> response = new java.util.HashMap<>();
        response.put("message", "Posicion desactivado.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
