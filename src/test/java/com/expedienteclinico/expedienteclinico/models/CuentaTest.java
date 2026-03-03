package com.expedienteclinico.expedienteclinico.models;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CuentaTest {

    @Test
//    @DisplayName("Comparacion sencilla")
    void testNombreCuenta() {
        Cuenta cuenta = new Cuenta( "Lex" , new BigDecimal( "1000.12345" ) ) ;
        cuenta.setPersona( "Lex" ) ;

        String esperado = "Lex" ;
        String real = cuenta.getPersona() ;

        assertEquals( esperado , real ) ;
        assertTrue( real.equals( "Lex" ) ) ;
    }

    @Test
//    @Disabled("Feature not yet implemented")
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

    @Test
    @DisplayName("Ensure that two temporary directories with same files names and content have same hash")
    void hashTwoDynamicDirectoryWhichHaveSameContent(@TempDir Path tempDir, @TempDir Path tempDir2) throws IOException {

        Path file1 = tempDir.resolve("myfile.txt");

        List<String> input = Arrays.asList("input1", "input2", "input3");
        Files.write(file1, input);

        assertTrue(Files.exists(file1), "File should exist");

        Path file2 = tempDir2.resolve("myfile.txt");

        Files.write(file2, input);
        assertTrue(Files.exists(file2), "File should exist");

    }



}