package com.expedienteclinico.expedienteclinico.services.common;

import com.expedienteclinico.expedienteclinico.models.common.HealthModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.net.InetAddress;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.sql.DataSource;
import java.sql.Connection;
@Service
public class ConnectivityService {

    @Autowired
    private DataSource dataSource;

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


    // Metod para checar la BB DD
    public String checkDatabase() {
        try (Connection conn = dataSource.getConnection()) {
            return conn.isValid(2) ? "Connected" : "Disconnected";
        } catch (SQLException e) {
            return "Disconnected";
        }
    }

    // Metod principal que genera el reporte con el nuevo modelo
    public HealthModel getSystemHealth() {
        String dbStatus = checkDatabase();

        // Usamos la logica de internet
        boolean internetUp;
        long latency;
        try {
            long start = System.currentTimeMillis();
            internetUp = InetAddress.getByName(TEST_HOST).isReachable(2000);
            latency = System.currentTimeMillis() - start;
        } catch (Exception e) {
            internetUp = false;
            latency = -1;
        }

        String internetState = !internetUp ? "Offline" : (latency > 500 ? "Unstable" : "Stable");
        String overallStatus = (dbStatus.equals("Connected") && internetUp) ? "UP" : "DEGRADED";

        return new HealthModel(
                overallStatus,
                dbStatus,
                internetState,
                latency,
                LocalDateTime.now(),
                null
        );
    }

}
