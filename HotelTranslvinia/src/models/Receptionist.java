package models;

import main.Database;
import models.ProxyFiles.ProxyResidentDataFetcher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**
 * obj Resident -> add , edit , delete , viewdetails [name ,stayDuration ,totalcost() ,contact_info]
 * assign room based on availability
 * obj Room ->
 * calcCost basedon Room type , nights ,boarding option
 * */
public class Receptionist extends User{

    private int salary;
    private String phone;
    private Resident resident;
    private Room roomObj;
    private List<Resident> residentList;
    private ProxyResidentDataFetcher fetcher;
    public Receptionist() {
        super();
        this.residentList = new ArrayList<>(); // Initialize the resident list
       this.resident = new Resident(); // Initialize the resident object
      this.fetcher = new ProxyResidentDataFetcher();
        this.residentList = fetcher.fetchResidents();
        //fetcher = new ProxyResidentDataFetcher(resident.getResidentName());
    }

    // Getters and Setters
    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public void addResident(String name, String phone, int durationStay, double totalCost) {
        Resident resident = new Resident();

        // Set resident details
        resident.setResidentName(name);
        resident.setResidentPhone(phone);
        resident.setDurationStay(durationStay);
        resident.setTotalCost(totalCost);

        // Add resident to the local list
        residentList.add(resident);

        // Add the resident to the database
        addResidentToDatabase(resident);

        System.out.println("Resident: " + name + " added successfully.");
    }
    // Database insertion logic
    private void addResidentToDatabase(Resident resident) {
        try (Connection connection = Database.getConnection()) {
            String query = "INSERT INTO resident (name, phone, duration_stay, total_cost) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, resident.getResidentName());
            statement.setString(2, resident.getResidentPhone());
            statement.setInt(3, resident.getDurationStay());
            statement.setDouble(4, resident.getTotalCost());

            statement.executeUpdate();
            System.out.println("Resident added to the database successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error adding resident to the database: " + e.getMessage());
        }
    }
    // Edit an existing resident's details
    public void editResident(String residentName, String newPhone, int newDurationStay, double newTotalCost) {
        for (Resident resident : residentList) {

            if(resident instanceof Resident && resident.getResidentName().equals(residentName)) {

                // Update in-memory data
                resident.setResidentPhone(newPhone);
                resident.setDurationStay(newDurationStay);
                resident.setTotalCost(newTotalCost);

                // Update in the database
                editResidentToDatabase(resident);
                return;
            }
        }
        System.out.println("Resident not found: " + residentName);
    }
    // Database code for editing a resident
    private void editResidentToDatabase(Resident resident) {
        try (Connection connection = Database.getConnection()) {
            String query = "UPDATE resident SET phone = ?, duration_stay = ?, total_cost = ? WHERE name = ?";
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, resident.getResidentPhone());
            statement.setInt(2, resident.getDurationStay());
            statement.setDouble(3, resident.getTotalCost());
            statement.setString(4, resident.getResidentName());

            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Resident details updated successfully in the database: " + resident.getResidentName());
            } else {
                System.out.println("Failed to update the resident details in the database: " + resident.getResidentName());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Delete a resident from the resident list
    public void deleteResident(String residentName) {
        for (Resident resident : residentList) {
            if (resident.getResidentName().equals(residentName)) {
                // Remove from in-memory list
                residentList.remove(resident);

                // Remove from the database
                deleteResidentFromDatabase(residentName);
                return;
            }
        }
        System.out.println("Resident not found: " + residentName);
    }
    // Database code for deleting a resident
    private void deleteResidentFromDatabase(String residentName) {
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

    // View details of all residents
    public void viewResidentDetails() {
        System.out.println("List of Residents:");
        for (Resident resident : residentList) {
            System.out.println("Name: " + resident.getResidentName() +
                    ", Phone: " + resident.getResidentPhone() +
                    ", Duration of Stay: " + resident.getDurationStay() +
                    ", Total Cost: $" + resident.getTotalCost());
        }
    }

    //------TESTCODE------///

//        public static void main(String[] args) {

//            Receptionist Receptionist = new Receptionist();

            // Test adding residents
            //System.out.println("Testing Add Functionality:");
            //Receptionist.addResident("Jihaaad", "01112345678", 5, 250.00);
            //Receptionist.addResident("salmaa", "01234567890", 3, 150.00);
            //Receptionist.addResident("ali", "01098765432", 7, 350.00);

            // View the list of residents in the system
//            System.out.println("\nCurrent List of Residents:");
//            Receptionist.viewResidentDetails();

            // Test editing a resident's details
           //System.out.println("\nTesting Edit Functionality:");
           // Receptionist.editResident("John Doe", "01199999999", 100, 300.00);

            // View the updated list of residents
           // System.out.println("\nUpdated List of Residents:");
            //Receptionist.viewResidentDetails();

            // Test deleting a resident
//           System.out.println("\nTesting Delete Functionality:");
//           Receptionist.deleteResident("Jane Smith");
//
//            // View the final list of residents
//          System.out.println("\nFinal List of Residents:");
//           Receptionist.viewResidentDetails();
//        }


}
