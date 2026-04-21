package com.nexuscore.security;

import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
public class TenantConnectionProvider implements MultiTenantConnectionProvider<String> {

    private final DataSource dataSource;

    public TenantConnectionProvider(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Connection getAnyConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public Connection getConnection(String tenantIdentifier) throws SQLException {
        System.out.println(">>> [PASO 3 - MYSQL] Forzando conexión física a esquema: " + tenantIdentifier);         Connection connection = getAnyConnection();
        // ESTA ES LA MAGIA: Cambia el esquema físico basado en el token JWT
        connection.createStatement().execute("USE " + tenantIdentifier);
        return connection;
    }

    @Override
    public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
        // Saneamiento: Regresa a la maestra antes de devolverla al Pool
        connection.createStatement().execute("USE his_master");
        connection.close();
    }

    @Override
    public boolean supportsAggressiveRelease() { return true; }

    @Override
    public boolean isUnwrappableAs(Class<?> unwrapType) { return false; }

    @Override
    public <T> T unwrap(Class<T> unwrapType) { return null; }
}