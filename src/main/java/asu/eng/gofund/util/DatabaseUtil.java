package asu.eng.gofund.util;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class DatabaseUtil {

    public static final JdbcTemplate jdbcTemplate;

    static {
        // Database configuration
        // TODO: Add your database configuration here
        String driverClassName = "org.postgresql.Driver";
        String url = "jdbc:postgresql://localhost:5432/<DB name>";
        String username = "<your username>";
        String password = "<your password>";

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        jdbcTemplate = new JdbcTemplate(dataSource);
    }
}
