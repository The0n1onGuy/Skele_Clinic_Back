package com.expedienteclinico.expedienteclinico.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class Banco {
    private  String nombre ;
    private List<Cuenta> cuentas;

    public Banco(){
        cuentas = new ArrayList<>();
    }

    public void transferir(Cuenta origen , Cuenta destino , BigDecimal monto){
        origen.debito(monto);
        destino.credito(monto);

    }

    public void addCuenta(Cuenta cuenta) {
        cuenta.setBanco(this);
        cuentas.add(cuenta);
    }
}
