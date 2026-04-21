package com.nexuscore.controllers.rrhh;

import com.nexuscore.beans.rrhh.SchedulesObject;
import com.nexuscore.payload.response.ResponseFactory;
import com.nexuscore.services.rrhh.SchedulesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
// @CrossOrigin(origins = "*")
@RequestMapping("/api/his/v1/rrhh/schedules/")
public class
SchedulesController {
    @Autowired
    SchedulesService schedulesService;

    @GetMapping("get")
    public ResponseEntity<Map<String, Object>> getAll() {
        List<SchedulesObject> lista = schedulesService.getAll();
        return new ResponseEntity<Map<String, Object>>( ResponseFactory.getSuccessOnGetAllResponse(lista) , HttpStatus.OK );

    }
}
