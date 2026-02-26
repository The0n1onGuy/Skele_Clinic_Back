package com.expedienteclinico.expedienteclinico.models;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CuentaTest {
    @Test
    void testNombreCuenta(){
        Cuenta cuenta = new Cuenta("Onion", new BigDecimal("1500.12345"));
        cuenta.setPersona("Onion");
        String esperado = "Onion";
        String real = cuenta.getPersona();

        assertEquals(esperado, real);
        assertTrue(real.equals("Onion"));

    }

    @Test
    void testSaldoCuenta(){
        Cuenta cuenta = new Cuenta("Onion", new BigDecimal("1500.12345"));
        assertEquals(1500.12345, cuenta.getSaldo().doubleValue());
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0 );
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0 );

    }

    @Test
    void testReferenciaDeCuenta(){
        //Cuenta cuenta = new Cuenta("Jonh Helldiver", new BigDecimal("1500.9997"));
        Cuenta cuenta1 = new Cuenta("Reynaulth", new BigDecimal("1500.5555"));
        Cuenta cuenta2 = new Cuenta("Reynaulth", new BigDecimal("1500.5555"));
        //assertNotEquals(cuenta1, cuenta2);
        assertEquals(cuenta1, cuenta2);
    }
}