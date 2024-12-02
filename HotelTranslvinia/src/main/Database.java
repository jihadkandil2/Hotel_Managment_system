package main;

import java.sql.*;

public class Database {
    private static Connection connection;
    private static final String URL = "jdbc:mysql://localhost:3306/hotel_management";
    private static final String USER = "root"; // Change this if necessary
    private static final String PASSWORD = "1542003"; // Use your MySQL password

    private Database() {}
    // Singleton pattern to get the database connection
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        System.out.println("Connection to database established successfully!");
        return connection;
    }

    // Close connection
    public static void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
