package com.expedienteclinico.expedienteclinico.services.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.net.InetAddress;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.sql.DataSource;
import java.sql.Connection;
@Service
public class ConnectivityService {
    // Usamos el DNS de Google (8.8.8.8) o Cloudflare (1.1.1.1) para probar internet real
    private static final String TEST_HOST = "8.8.8.8";
    private static final int TIMEOUT_MS = 2000;

    public Map<String, Object> checkFullConnectivity() {
        Map<String, Object> report = new LinkedHashMap<>();

        long startTime = System.currentTimeMillis();
        boolean isReachable = false;

        try {
            isReachable = InetAddress.getByName(TEST_HOST).isReachable(TIMEOUT_MS);
        } catch (IOException e) {
            isReachable = false;
        }

        long endTime = System.currentTimeMillis();
        long latency = endTime - startTime;

        report.put("internet_access", isReachable);
        report.put("latency_ms", latency);

        // Lógica de diagnóstico
        if (!isReachable) {
            report.put("status", "OFFLINE");
            report.put("diagnosis", "Sin salida a internet. Verifique cables o proveedor.");
        } else if (latency > 500) {
            report.put("status", "UNSTABLE");
            report.put("diagnosis", "Conexión lenta detectada. Riesgo de timeout en transacciones.");
        } else {
            report.put("status", "STABLE");
            report.put("diagnosis", "Conexión óptima.");
        }

        return report;
    }

    @Autowired
    private DataSource dataSource;

    public boolean isDatabaseConnected() {
        try(Connection connection = dataSource.getConnection()){
            //Ejecutamos una consulta para estar seguros
            return connection.isValid(2);
        } catch (Exception e){
            return false;
        }
    }

}
