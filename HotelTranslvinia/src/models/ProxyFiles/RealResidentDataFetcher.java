package models.ProxyFiles;

import main.Database; // Import your Database class
import models.Receptionist;
import models.Resident;
import models.Room;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class RealResidentDataFetcher implements ResidentDataFetcher{
    private  String residentName;
    private Receptionist receptionist;
    private Room room;
    List<Resident> residents;
    public RealResidentDataFetcher() {
       // this.residentName = residentName;
    }
    @Override
    public List<Resident> fetchResidents() {
        residents= new ArrayList<>();

        String query = "SELECT name, phone, duration_stay, total_cost ,assignedRoom FROM resident"; // Fetch all residents

        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Resident resident = new Resident();
                resident.setResidentName(resultSet.getString("name"));
                resident.setResidentPhone(resultSet.getString("phone"));
                resident.setDurationStay(resultSet.getInt("duration_stay"));
                resident.setTotalCost(resultSet.getDouble("total_cost"));
                resident.setRoomType(resultSet.getString("assignedRoom"));
                residents.add(resident);
            }
        } catch (Exception e) {
            System.err.println("Error fetching resident data: " + e.getMessage());
            e.printStackTrace(); // Log detailed stack trace for debugging
        }
        return residents;
    }

    @Override
    public void editResidentToDatabase(Resident resident , String newName ,String newPhone) {
        try (Connection connection = Database.getConnection()) {
            String query = "UPDATE resident SET phone = ? , name=? WHERE name = ?";
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, newPhone);
            statement.setString(2, newName);
            statement.setString(3, resident.getResidentName());


            int rowsUpdated = statement.executeUpdate();
            //Depugging
            if (rowsUpdated > 0) {
                System.out.println("Resident details updated successfully in the database: " + newName);
            } else {
                System.out.println("Failed to update the resident details in the database: " + resident.getResidentName());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteResidentFromDatabase(String residentName) {
        try (Connection connection = Database.getConnection()) {
            String query = "DELETE FROM resident WHERE name = ?";
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, residentName);

            int rowsDeleted = statement.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Resident deleted successfully from the database: " + residentName);
            } else {
                System.out.println("Failed to delete the resident from the database: " + residentName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
