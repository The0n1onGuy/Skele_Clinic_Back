package com.expedienteclinico.expedienteclinico.models;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CuentaTest {

    @Test
    void testNombreCuenta() {
        Cuenta cuenta = new Cuenta( "Lex" , new BigDecimal( "1000.12345" ) ) ;
        cuenta.setPersona( "Lex" ) ;

        String esperado = "Lex" ;
        String real = cuenta.getPersona() ;

        assertEquals( esperado , real ) ;
        assertTrue( real.equals( "Lex" ) ) ;
    }

    @Test
    void testSaldoCuenta() {
        Cuenta cuenta = new Cuenta( "Lex" , new BigDecimal( "1000.12345" ) ) ;

        assertEquals( 1000.12345 , cuenta.getSaldo().doubleValue() ) ;
        assertFalse( cuenta.getSaldo().compareTo( BigDecimal.ZERO ) < 0 ) ;
        assertTrue( cuenta.getSaldo().compareTo( BigDecimal.ZERO ) > 0 ) ;
    }

    @Test
    void testReferenciaDeCuenta() {
        Cuenta cuenta =  new Cuenta( "John Doe" , new BigDecimal( "8900.9997" ) ) ;
        Cuenta cuenta2 = new Cuenta( "John Doe" , new BigDecimal( "8900.9997" ) ) ;

//        assertNotEquals( cuenta2 , cuenta ) ;
        assertEquals( cuenta2 , cuenta ) ;

    }



}