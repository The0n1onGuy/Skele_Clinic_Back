package com.expedienteclinico.expedienteclinico.models;

import com.expedienteclinico.expedienteclinico.exceptions.DineroInsuficienteException;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter

public class Cuenta {

    private String persona;
    private BigDecimal saldo;
    private  Banco banco;

    public Cuenta(String persona, BigDecimal saldo){

        this.persona = persona;
        this.saldo = saldo;

    }

    @Override
    public boolean equals( Object object){

        if (! (object instanceof Cuenta)){

            return false;

        }

        Cuenta c = ( Cuenta) object;
        if (this.persona == null || this.saldo == null){
            return false;
        }

        return this.persona.equals(c.getPersona()) && this.saldo.equals( c.getSaldo());

    }

    public void debito(BigDecimal monto) {
//        if (this.saldo.compareTo(monto) < 0) {
//            throw new DineroInsuficienteException("Tu ere poble no tene ifon");
//        }
        BigDecimal nuevoSaldo = this.saldo = this.saldo.subtract(monto);

        if (nuevoSaldo.compareTo(BigDecimal.ZERO)<0){
            throw new DineroInsuficienteException("Tu ere poble no tene ifon");
        }

    }

    public void credito (BigDecimal monto){
        this.saldo = this.saldo.add(monto);
    }

}
