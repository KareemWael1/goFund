package asu.eng.gofund.util;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class DatabaseUtil {

    private static JdbcTemplate connection = null;

    public static JdbcTemplate getConnection() {
        if (connection == null) {
            // Database configuration from environment variables
            String driverClassName = System.getenv("DB_DRIVER_CLASS_NAME");
            String url = System.getenv("DB_URL");
            String username = System.getenv("DB_USERNAME");
            String password = System.getenv("DB_PASSWORD");

            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName(driverClassName);
            dataSource.setUrl(url);
            dataSource.setUsername(username);
            dataSource.setPassword(password);

            connection = new JdbcTemplate(dataSource);
        }
        return connection;
    }
}
