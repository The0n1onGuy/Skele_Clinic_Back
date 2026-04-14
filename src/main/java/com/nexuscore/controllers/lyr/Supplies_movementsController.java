package com.nexuscore.controllers.lyr;


import com.nexuscore.beans.lyr.Supplies_movementsObject;
import com.nexuscore.payload.response.ResponseFactory;
import com.nexuscore.services.lyr.Supplies_movementsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

// @CrossOrigin( origins = "*" , maxAge = 3600 )
@RestController
@RequestMapping( "api/his/v1/movimiento_insumos-controller/" )
public class Supplies_movementsController {

    @Autowired
    Supplies_movementsService supplies_movementsService;

    @GetMapping( "all")
    public ResponseEntity<Map<String, Object>> getAll() {

        List<Supplies_movementsObject> empledos = supplies_movementsService.getAll();
        return new ResponseEntity<Map<String, Object>>(ResponseFactory.getSuccessOnGetAllResponse(empledos), HttpStatus.OK);
    }
}
