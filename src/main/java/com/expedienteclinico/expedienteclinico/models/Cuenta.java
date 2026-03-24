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
    private Banco banco;
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

    public void debito( BigDecimal debito ){

        BigDecimal nuevosaldo = this.saldo.subtract(debito);
        if ( nuevosaldo.compareTo(BigDecimal.ZERO) < 0 ){
            throw new DineroInsuficienteException("C jodido.");
        }

        this.saldo = nuevosaldo;

    }

    public void credito( BigDecimal credito ){

        this.saldo = this.saldo.add(credito);

    }
}
