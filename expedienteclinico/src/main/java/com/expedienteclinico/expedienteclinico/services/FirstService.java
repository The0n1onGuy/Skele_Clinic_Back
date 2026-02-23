package com.expedienteclinico.expedienteclinico.services;

import com.expedienteclinico.expedienteclinico.beans.FirstObject;
import com.expedienteclinico.expedienteclinico.models.FirstModel;
import com.expedienteclinico.expedienteclinico.payload.response.ResponseFactory;
import com.expedienteclinico.expedienteclinico.repositories.IFirstRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


//SERVICIO DEL MODELO FIRST MODEL
@Service
public class FirstService {
    @Autowired
    IFirstRepository iFirstRepository;

    //Usaras este metodo para ejecutar en tu controlador el enlace
    public List<FirstModel> getAll() {
        return iFirstRepository.findAll() ;
    }

    public ArrayList<Object> getFirstList() {
        ArrayList<Object> objectOfObjects = new ArrayList<>();

        FirstObject a = new FirstObject();
        a.setNombre("Onion");
        a.setDireccion("Villas otoch");
        a.setEdad( 21 );
        a.setTelefono("9999977999");

        FirstObject b = new FirstObject();
        b.setNombre("AmoGus");
        b.setDireccion("Villas del Mal");
        b.setEdad( 23 );
        b.setTelefono("9499999999");

        FirstObject c = new FirstObject();
        c.setNombre("Skeleton");
        c.setDireccion("Villas Dorados");
        c.setEdad( 25 );
        c.setTelefono("96699999799");

        FirstObject d = new FirstObject();
        d.setNombre("Springer");
        d.setDireccion("Villas Springs");
        d.setEdad( 33 );
        d.setTelefono("9999999769");
        objectOfObjects.add(a);
        objectOfObjects.add(b);
        objectOfObjects.add(c);
        objectOfObjects.add(d);

        return objectOfObjects;
    }

    public Object nuevo(FirstObject firstObject){
        ArrayList <Object> a  = new ArrayList<>();
        a.add("draw: 1");
        a.add("recordsTotal: 57");
        a.add("recordsFiltered: 57");
        a.add("data:");
        a.add(firstObject);
        return a;
    }



    public ResponseEntity<Map <String, Object > > updateData(FirstModel firstModel, BindingResult result, Long id){
        FirstModel modelToChange = null;
        if (result.hasErrors()) return new ResponseEntity<Map <String, Object >>(ResponseFactory.getErrorResponse(result), HttpStatus.BAD_REQUEST);
        try {
            modelToChange = iFirstRepository.findById(id).orElse(null);
            if (modelToChange == null) return new ResponseEntity<Map <String, Object >>( ResponseFactory.getNotFoundResponse(modelToChange), HttpStatus.NOT_FOUND );
            modelToChange.setNombre(firstModel.getNombre());
            modelToChange.setApellido(firstModel.getApellido());

            iFirstRepository.save(modelToChange);
        } catch (DataAccessException e ){
            return new ResponseEntity<Map <String, Object >>( ResponseFactory.getErrorToUpdateResponse(firstModel), HttpStatus.INTERNAL_SERVER_ERROR );
        }
        return new ResponseEntity<Map <String, Object >>( ResponseFactory.getUpdateResponse(modelToChange), HttpStatus.CREATED );
    }
}