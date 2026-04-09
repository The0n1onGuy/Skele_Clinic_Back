package com.expedienteclinico.expedienteclinico.services.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.InetAddress;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConnectivityServiceTest {

    @InjectMocks
    private ConnectivityService connectivityService;

    @Test
    @DisplayName("CONECTIVIDAD - Debería detectar estado STABLE cuando el ping es rápido")
    void testCheckConnectivityStable() throws Exception {
        // MockedStatic permite simular clases estáticas de Java como InetAddress
        try (MockedStatic<InetAddress> mockedInetAddress = mockStatic(InetAddress.class)) {
            InetAddress mockAddress = mock(InetAddress.class);

            // Configuramos: Google (8.8.8.8) es alcanzable en menos de 2 segundos
            mockedInetAddress.when(() -> InetAddress.getByName("8.8.8.8")).thenReturn(mockAddress);
            when(mockAddress.isReachable(anyInt())).thenReturn(true);

            // Ejecución
            Map<String, Object> result = connectivityService.checkFullConnectivity();

            // Verificación
            assertEquals("STABLE", result.get("status"));
            assertTrue((Boolean) result.get("internet_access"));
            assertEquals("Conexión óptima.", result.get("diagnosis"));
        }
    }

    @Test
    @DisplayName("CONECTIVIDAD - Debería detectar estado OFFLINE cuando no hay respuesta")
    void testCheckConnectivityOffline() throws Exception {
        try (MockedStatic<InetAddress> mockedInetAddress = mockStatic(InetAddress.class)) {
            InetAddress mockAddress = mock(InetAddress.class);

            mockedInetAddress.when(() -> InetAddress.getByName(anyString())).thenReturn(mockAddress);
            // Simulamos que isReachable devuelve false (timeout o cable desconectado)
            when(mockAddress.isReachable(anyInt())).thenReturn(false);

            Map<String, Object> result = connectivityService.checkFullConnectivity();

            assertEquals("OFFLINE", result.get("status"));
            assertFalse((Boolean) result.get("internet_access"));
            assertTrue(result.get("diagnosis").toString().contains("Sin salida a internet"));
        }
    }

    @Test
    @DisplayName("CONECTIVIDAD - Debería detectar estado UNSTABLE si hay latencia simulada")
    void testCheckConnectivityUnstable() throws Exception {
        try (MockedStatic<InetAddress> mockedInetAddress = mockStatic(InetAddress.class)) {
            InetAddress mockAddress = mock(InetAddress.class);

            mockedInetAddress.when(() -> InetAddress.getByName(anyString())).thenReturn(mockAddress);

            // Simulamos un retraso real en la ejecución para que la latencia supere los 500ms
            when(mockAddress.isReachable(anyInt())).thenAnswer(invocation -> {
                Thread.sleep(600); // Forzamos 600ms de espera
                return true;
            });

            Map<String, Object> result = connectivityService.checkFullConnectivity();

            assertEquals("UNSTABLE", result.get("status"));
            assertTrue((Long) result.get("latency_ms") >= 500);
            assertTrue(result.get("diagnosis").toString().contains("Conexión lenta"));
        }
    }

    @Test
    @DisplayName("Test de conexion con Base de Datos")
    void testDatabaseConnectionSuccess(){
        boolean isAlive = connectivityService.isDatabaseConnected();
        assertTrue(isAlive, "La base de datos funoncia");
    }
}