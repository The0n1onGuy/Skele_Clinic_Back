package com.expedienteclinico.expedienteclinico.beans.rpbi;
import lombok.Data;

@Data
public class RpbiEstadoFisicoObject {
    private Long id;
    private String nombre;
    private String unidadMedida;
    private Long estatusId;
    private String estatusNombre;
}
