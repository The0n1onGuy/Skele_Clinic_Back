package com.nexuscore.services.system;

import com.nexuscore.models.system.HttpStatusCode;
import com.nexuscore.repositories.system.IHttpStatusCodeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class HttpStatusService {

    // Repositorio para consultar códigos HTTP en la base de datos
    @Autowired
    private IHttpStatusCodeRepository repository;

    // Metodo para obtener un código HTTP desde la base de datos
    public HttpStatusCode getByCodeSafe(int code) {

        return repository.findByCode(code)
                .orElseGet(() -> {
                    HttpStatusCode unknown = new HttpStatusCode();
                    unknown.setCode(code);
                    unknown.setDescription("Código no catalogado");
                    return unknown;
                });
    }

    // Metodo para convertir un código a HttpStatus de Spring
    public HttpStatus getSpringStatus(int code) {

        // Primero valida que exista en la base de datos
        getByCodeSafe(code);

        // Convierte el código numérico a enum de Spring
        return HttpStatus.valueOf(code);
    }
}