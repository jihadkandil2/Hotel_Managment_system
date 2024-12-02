package models.ProxyFiles;

import main.Database; // Import your Database class
import models.Receptionist;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RealReciptionistDataFetcher implements ReceptionistDataFetcher {

    @Override
    public List<Receptionist> fetchReceptionists() {
        List<Receptionist> receptionists = new ArrayList<>();
        String query = "SELECT userName, email, phone , password,role ,Salary FROM users WHERE role = 'receptionist'";

        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                // Create a Receptionist object and populate it with data from the database
                Receptionist receptionist = new Receptionist();
                receptionist.setUserName(resultSet.getString("userName"));
                receptionist.setEmail(resultSet.getString("email"));
                receptionist.setPhone(resultSet.getString("phone"));
                receptionist.setPassword(resultSet.getString("password"));
                receptionist.setRole(resultSet.getString("role"));
                receptionist.setSalary(resultSet.getInt("salary"));
                receptionists.add(receptionist);
            }

            System.out.println("Fetched receptionist data from the database.");
        } catch (Exception e) {
            System.err.println("Error fetching receptionist data: " + e.getMessage());
            e.printStackTrace(); // Log detailed stack trace for debugging
        }

        return receptionists;
    }
}
