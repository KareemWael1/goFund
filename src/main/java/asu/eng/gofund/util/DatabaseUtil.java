package asu.eng.gofund.util;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class DatabaseUtil {

    private static JdbcTemplate connection = null;

    public static JdbcTemplate getConnection() {
        if (connection == null) {
            // Try Spring properties first, then fallback to environment variables
            String driverClassName = System.getProperty("spring.datasource.driver-class-name");
            String url = System.getProperty("spring.datasource.url");
            String username = System.getProperty("spring.datasource.username");
            String password = System.getProperty("spring.datasource.password");

            // Fallback to environment variables if Spring properties are not set
            driverClassName = driverClassName != null ? driverClassName : System.getenv("DB_DRIVER_CLASS_NAME");
            url = url != null ? url : System.getenv("DB_URL");
            username = username != null ? username : System.getenv("DB_USERNAME");
            password = password != null ? password : System.getenv("DB_PASSWORD");

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