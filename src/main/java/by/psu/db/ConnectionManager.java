package by.psu.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class ConnectionManager implements AutoCloseable {
    private final Connection connection;

    public ConnectionManager() {
        try {
            Class.forName("org.postgresql.Driver");
            var props = loadProperties();
            this.connection = DriverManager.getConnection(
                    props.getProperty("url"),
                    props.getProperty("user"),
                    props.getProperty("password"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Properties loadProperties() throws IOException {
        var propertiesStream = ConnectionManager.class.getResourceAsStream("/db.properties");
        var properties = new Properties();
        properties.load(propertiesStream);
        return properties;
    }

    public Connection getConnection() {
        return connection;
    }

    @Override
    public void close() throws Exception {
        if (connection == null) {
            return;
        }
        connection.close();
    }
}
