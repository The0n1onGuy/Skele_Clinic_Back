package com.expedienteclinico.expedienteclinico.services;

import com.expedienteclinico.expedienteclinico.beans.FirstObject;
import com.expedienteclinico.expedienteclinico.models.FirstModel;
import com.expedienteclinico.expedienteclinico.repositories.IFirstRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FirstService {
    @Autowired
    IFirstRepository iFirstRepository;
    public ArrayList<Object> getFirstList() {
        ArrayList<Object> objectOfObjects = new ArrayList<>();

        FirstObject a = new FirstObject();
        a.setNombre("Onion");
        a.setDireccion("Villas otoch");
        a.setEdad( 21 );
        a.setTelefono("9999999999");

        FirstObject b = new FirstObject();
        b.setNombre("AmoGus");
        b.setDireccion("Villas del Mal");
        b.setEdad( 23 );
        b.setTelefono("9499999999");

        FirstObject c = new FirstObject();
        c.setNombre("Skeleton");
        c.setDireccion("Villas Dorados");
        c.setEdad( 25 );
        c.setTelefono("9669999999");

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

    public List<FirstModel> getAll() {
        return iFirstRepository.findAll() ;
    }
}
