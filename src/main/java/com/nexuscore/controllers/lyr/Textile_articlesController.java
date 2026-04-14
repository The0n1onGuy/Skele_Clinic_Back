package com.nexuscore.controllers.lyr;

import com.nexuscore.beans.lyr.Textile_articlesObject;
import com.nexuscore.payload.response.ResponseFactory;
import com.nexuscore.services.lyr.Textile_articlesService;
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
@RequestMapping( "api/his/v1/articulos_textiles-controller/" )
public class Textile_articlesController {

    @Autowired
    Textile_articlesService Textile_articlesService;

    @GetMapping( "all")
    public ResponseEntity<Map<String, Object>> getAll() {

        List<Textile_articlesObject> articles = Textile_articlesService.getAll();
        return new ResponseEntity<Map<String, Object>>( ResponseFactory.getSuccessOnGetAllResponse(articles) , HttpStatus.OK );
    }
}
