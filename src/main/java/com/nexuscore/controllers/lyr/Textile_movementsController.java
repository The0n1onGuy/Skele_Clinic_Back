package com.nexuscore.controllers.lyr;


import com.nexuscore.beans.lyr.Textile_movementsObject;
import com.nexuscore.payload.response.ResponseFactory;
import com.nexuscore.services.lyr.Textile_movementsService;
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
@RequestMapping( "api/his/v1/movimiento_textiles-controller/" )
public class Textile_movementsController {

    @Autowired
    private Textile_movementsService textile_movementsService;

    @GetMapping( "all")
    public ResponseEntity<Map<String, Object>> getAll() {

        List<Textile_movementsObject> textile = textile_movementsService.getAll();
        return new ResponseEntity<Map<String, Object>>( ResponseFactory.getSuccessOnGetAllResponse(textile) , HttpStatus.OK );
    }
}
