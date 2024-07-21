package org.example;

import org.example.factories.ConnectionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import static org.example.Main.initializeDatabase;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class DatabaseConnectionTest {

    protected static Connection connection;

    @BeforeEach
    public void init() throws SQLException, IOException {
        UserContext.getInstance().setUserId(1);
        ConnectionFactory.setEnvironment(ConnectionFactory.Environment.TEST);
        connection = ConnectionFactory.getConnection();
        initializeDatabase(connection);
    }

    @AfterAll
    public static void tearDown() throws SQLException {
        connection.close();
    }

    @Test
    public void testDBConnection() {
        assertNotNull(connection);
    }
}
