package org.example.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DBPool {
    private static final HikariConfig config = new HikariConfig();
    private static final HikariDataSource ds;

    static {
        config.setJdbcUrl("jdbc:mysql://localhost:3306/TravelAgencyBD?useSSL=false&serverTimezone=UTC");
        config.setUsername("root");
        config.setPassword("arishasql");
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setIdleTimeout(30000);
        config.setConnectionTimeout(10000);
        config.setLeakDetectionThreshold(2000);

        ds = new HikariDataSource(config);
    }

    private DBPool() {}

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}