package com.expedienteclinico.expedienteclinico.models;

import com.expedienteclinico.expedienteclinico.exceptions.DineroInsuficienteException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.test.context.TestPropertySource;

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

//    @Test
//    @DisplayName("Ensure that two temporary directories with same files names and content have same hash")
//    void hashTwoDynamicDirectoryWhichHaveSameContent(@TempDir Path tempDir, @TempDir Path tempDir2) throws IOException {
//
//        Path file1 = tempDir.resolve("myfile.txt");
//
//        List<String> input = Arrays.asList("input1", "input2", "input3");
//        Files.write(file1, input);
//
//        assertTrue(Files.exists(file1), "File should exist");
//
//        Path file2 = tempDir2.resolve("myfile.txt");
//
//        Files.write(file2, input);
//        assertTrue(Files.exists(file2), "File should exist");
//
//    }

    @Test
    public void testDebidotCuenta (){

        Cuenta cuenta = new Cuenta("Alex", new BigDecimal( "1000.12345" ) ) ;
        cuenta.debito(new BigDecimal( 100 )) ;
        assertNotNull(cuenta.getSaldo() );
        assertEquals( 900 , cuenta.getSaldo().intValue() ) ;
        assertEquals( "900.12345" , cuenta.getSaldo().toPlainString() ) ;

    }

    @Test
    public void testCreditoCuenta (){

        Cuenta cuenta = new Cuenta("Alex", new BigDecimal( "1000.12345" ) ) ;
        cuenta.credito(new BigDecimal( 100 )); ;
        assertNotNull(cuenta.getSaldo() );
        assertEquals( 1100 , cuenta.getSaldo().intValue() ) ;
        assertEquals( "1100.12345" , cuenta.getSaldo().toPlainString() ) ;

    }

    @Test
    void testDineroInsuficienteExceptionCuenta(){

        Cuenta cuenta = new Cuenta("Alex", new BigDecimal( "1000.12345" ) ) ;

        Exception exception = assertThrows( DineroInsuficienteException.class , () ->{

            cuenta.debito(new BigDecimal( "1500" )) ;

        });

        String actual = exception.getMessage();
        String esperado = "C jodido.";

        assertEquals( esperado , actual ) ;

    }

    @Test
    void tesTransferirDineroCuentas(){

        Cuenta cuenta1 = new Cuenta("Alex", new BigDecimal( "1000.01" ) ) ;
        Cuenta cuenta2 = new Cuenta("Darikson", new BigDecimal( "1500.01" ) ) ;

        Banco banco = new Banco();
            banco.setNombre("Mercado Pago");
            banco.transferir( cuenta2, cuenta1, new BigDecimal("500" ) );

        assertEquals( "1000.01", cuenta2.getSaldo().toPlainString() ) ;
        assertEquals( "1500.01", cuenta1.getSaldo().toPlainString() ) ;
    }

    @Test
    void testRelacionBancoCuentas() {

        Cuenta cuenta1 = new Cuenta("Alex", new BigDecimal("1000.01"));
        Cuenta cuenta2 = new Cuenta("Darikson", new BigDecimal("1500.01"));

        Banco banco = new Banco();

        banco.addCuenta(cuenta1);
        banco.addCuenta(cuenta2);


        banco.setNombre("Mercado Pago");
        banco.transferir(cuenta2, cuenta1, new BigDecimal("500"));

        assertEquals("1000.01", cuenta2.getSaldo().toPlainString());
        assertEquals("1500.01", cuenta1.getSaldo().toPlainString());

        assertEquals(2, banco.getCuentas().size());

        assertEquals("Mercado Pago", cuenta1.getBanco().getNombre());

        assertEquals("Darikson", banco.getCuentas().stream()
                .filter(c -> c.getPersona().equals("Darikson"))
                .findFirst()
                .get().getPersona()
        );

        assertTrue(banco.getCuentas().stream()
                .filter(c -> c.getPersona().equals("Darikson"))
                .findFirst().isPresent());

        assertTrue( banco.getCuentas().stream()
                .anyMatch(c -> c.getPersona().equals("Alex"))
        );

    }

}












