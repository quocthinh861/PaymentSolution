package org.example.factories;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    private static final String JDBC_URL = "jdbc:h2:mem:mydb;DB_CLOSE_DELAY=-1";
    private static final String JDBC_TEST_URL = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
    private static final String JDBC_USER = "sa";
    private static final String JDBC_PASSWORD = "";

    public enum Environment {
        DEV,
        TEST
    }

    private static Environment currentEnvironment = Environment.DEV;

    public static void setEnvironment(Environment environment) {
        currentEnvironment = environment;
    }

    public synchronized static Connection getConnection() throws SQLException {
        String url;
        switch (currentEnvironment) {
            case TEST:
                url = JDBC_TEST_URL;
                break;
            default:
                url = JDBC_URL;
                break;
        }
        return DriverManager.getConnection(url, JDBC_USER, JDBC_PASSWORD);
    }
}
