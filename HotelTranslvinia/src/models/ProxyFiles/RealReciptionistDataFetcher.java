package models.ProxyFiles;

import main.Database; // Import your Database class
import models.Receptionist;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    @Override
    public void addReceptionistToDatabase(Receptionist receptionist) {
        try (Connection connection = Database.getConnection()) {
            String query = "INSERT INTO users (userName, email, phone, password, role ,Salary) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, receptionist.getUserName());

            statement.setString(2, receptionist.getEmail());
            statement.setString(3, receptionist.getPhone());
            statement.setString(4, receptionist.getPassword());
            statement.setString(5, receptionist.getRole());
            statement.setInt(6, receptionist.getSalary());
            statement.executeUpdate();
            System.out.println("Receptionist [ " + receptionist.getUserName()+" ] added to the database successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void editReceptionistToDatabase(Receptionist receptionist) {
        try (Connection connection = Database.getConnection()) {
            String query = "UPDATE users SET  password = ? , Salary = ?, phone = ? WHERE userName = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, receptionist.getPassword());
            statement.setInt(2, receptionist.getSalary());
            statement.setString(3, receptionist.getPhone());
            statement.setString(4, receptionist.getUserName()); // Set the value for userName (fourth parameter)

            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Receptionist details updated successfully in the database: " + receptionist.getUserName());
            } else {
                System.out.println("Failed to update the receptionist details in the database: " + receptionist.getUserName());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteReceptionistFromDatabase(String userName) {
        try (Connection connection = Database.getConnection()) {
            String query = "DELETE FROM users WHERE userName = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, userName); // Set the value for userName

            int rowsDeleted = statement.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Receptionist deleted successfully from the database: " + userName);
            } else {
                System.out.println("Failed to delete the receptionist from the database: " + userName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
