package com.nexushiscore.beans.morgue;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MorgueObject {
    private String nombreFinado;
    private Integer edad;
    private String causaDefuncion;
    private String fechaIngreso;
    private Integer numeroGaveta;
}