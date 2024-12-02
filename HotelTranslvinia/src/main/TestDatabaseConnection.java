package main;

import java.sql.*;

public class TestDatabaseConnection {
    public static void main(String[] args) {
        // MySQL connection URL, username, and password
        String url = "jdbc:mysql://localhost:3306/hotel_management"; // Replace with your DB URL
        String user = "root";  // Your MySQL username (e.g., root)
        String password = "1542003"; // Your MySQL password

        // Establish connection
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connection to database established successfully!");
        } catch (SQLException e) {
            System.out.println("Failed to connect to the database.");
            e.printStackTrace();
        }
    }
}
