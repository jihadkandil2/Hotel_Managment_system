package models.ProxyFiles;

import main.Database; // Import your Database class
import models.Resident;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
public class RealResidentDataFetcher implements ResidentDataFetcher{
    private  String residentName;

    public RealResidentDataFetcher() {
       // this.residentName = residentName;
    }
    @Override
    public List<Resident> fetchResidents() {
        List<Resident> residents = new ArrayList<>();
        //String query = "SELECT name, phone, duration_stay, total_cost FROM resident WHERE name = ?";
        String query = "SELECT name, phone, duration_stay, total_cost FROM resident"; // Fetch all residents

        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Resident resident = new Resident();
                resident.setResidentName(resultSet.getString("name"));
                resident.setResidentPhone(resultSet.getString("phone"));
                resident.setDurationStay(resultSet.getInt("duration_stay"));
                resident.setTotalCost(resultSet.getDouble("total_cost"));
                residents.add(resident);
            }
            System.out.println("Fetched resident data for name: " + residentName);
        } catch (Exception e) {
            System.err.println("Error fetching resident data: " + e.getMessage());
            e.printStackTrace(); // Log detailed stack trace for debugging
        }

        return residents;

    }
}
