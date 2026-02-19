package com.expedienteclinico.expedienteclinico.beans.rpbi;
import lombok.Data;

@Data
public class RpbiEnvaseObject {
    private Long id;
    private String nombre;
    private String descripcion;
    private Long estatusId;
    private String estatusNombre;
}
