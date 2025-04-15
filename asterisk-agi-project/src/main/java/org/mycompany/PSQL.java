package org.mycompany;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;

public class PSQL {

    private static final String JDBC_DRIVER = "org.postgresql.Driver";
    private static final String URL = "jdbc:postgresql://localhost:5432/balance_users";  
    private static final String USER = "postgres";  
    private static final String PASSWORD = "123";  

    static {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("PostgreSQL Driver not found!", e);
        }
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("Error connecting to the database: " + e.getMessage(), e);
        }
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing the connection: " + e.getMessage());
            }
        }
    }

    public double getBalance(String msisdn) {
        double balance = -1;
        String sql = "SELECT balance FROM users WHERE msisdn = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, msisdn);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                balance = rs.getDouble("balance");
            }

            rs.close();
        } catch (SQLException e) {
            System.err.println("Error fetching balance: " + e.getMessage());
        }

        return balance;
    }
}
