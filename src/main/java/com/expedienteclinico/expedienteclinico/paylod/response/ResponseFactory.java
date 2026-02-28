package com.expedienteclinico.expedienteclinico.paylod.response;

import com.expedienteclinico.expedienteclinico.models.FirstModel;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ResponseFactory {
    public static Map< String, Object > getErrorResponse( BindingResult result ){
        Map< String, Object > response = new HashMap<>();

        List< String > lsErrors = result.getFieldErrors().stream()
                .map( err -> "El campo'" + err.getField() + "' " + err.getDefaultMessage())
                .collect(Collectors.toList());
        response.put("errors", lsErrors);

        return response;
    }


    public static Map<String, Object> getNotFoundResponse(Object object) {
            Map< String, Object> response = new HashMap<>();

            response.put( "message" , String.format( "&s no encontrado." , object.getClass().getSimpleName()));

            return response;
    }

    public static  Map< String, Object >  getErrorToUpdateResponse(Object object) {
        Map< String, Object> response = new HashMap<>();
        response.put("message", "");
        response.put(object.getClass().getSimpleName() .toLowerCase(), object);

        return response;
    }

    public static Map<String, Object> getUpdateResponse(Object object) {
        Map< String, Object> response = new HashMap<>();
        response.put("message", String.format("%s se actualizo correctamente.", object.getClass().getSimpleName()));
        response.put(object.getClass().getSimpleName() .toLowerCase(), object);
        return response;
    }

//    public static Map<String, Object> getSuccessOnGetAllResponse(Object data){
//        Map<String, Object> response = new LinkedHashMap<>();
//        response.put("message", String.format("Informacion obtenida"));
//        response.put("data", data);
//        return response;
//    }
}
