package models;

import java.util.ArrayList;
import java.util.List;
import main.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import models.ProxyFiles.ProxyReciptionistDataFetcher;


public class Manager extends User {
    Receptionist receptionist;
    Resident resident;
    private List<Receptionist> workers;
    private ProxyReciptionistDataFetcher fetcher = new ProxyReciptionistDataFetcher();

    private Manager() {
        super(); // Set default username and password
        this.workers = new ArrayList<>(); // Initialize the workers list
        workers = fetcher.fetchReceptionists();
    }

    //Singlton pattern to manage only one instance of manager can be created
    private static Manager instance;

    public static Manager getInstance() {
        if (instance == null) {
            instance = new Manager();
        }
        return instance;
    }

    public List<Receptionist> getWorkers() {
        return workers;
    }


    public void addReceptionist(String receptionistName, String email, String Phone, String Pass, String role, int salary) {
        receptionist = new Receptionist();

        receptionist.setUserName(receptionistName);
        receptionist.setEmail(email);
        receptionist.setPhone(Phone);
        receptionist.setPassword(Pass);
        receptionist.setRole(role);
        receptionist.setSalary(salary);

        workers.add(receptionist);
        // Add the receptionist to the database
        addReceptionistToDatabase(receptionist);
        System.out.println("Receptionist : " + receptionistName + " added successfully");
    }

    //DataBasecode
    private void addReceptionistToDatabase(Receptionist receptionist) {
        // Database insertion logic
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
            System.out.println("Receptionist added to the database successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //editDataBaseCode
    private void editReceptionistToDatabase(Receptionist receptionist) {
        // Update in the database
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

    // Edit an existing receptionist's details
    public void editReceptionist(String receptionistName, int newSalary, String newPass, String newPhone) {

        for (Receptionist worker : workers) {
            if (worker instanceof Receptionist && worker.getUserName().equals(receptionistName)) {
                // Update in-memory data
                worker.setPassword(newPass);
                worker.setSalary(newSalary);
                worker.setPhone(newPhone);

                editReceptionistToDatabase(worker);
                return;
            }
        }
        System.out.println("Receptionist not found: " + receptionistName);

    }

    // Delete a receptionist from the workers list
    public void deleteReceptionist(String receptionistName) {
        for (Receptionist worker : workers) {
            if (worker instanceof Receptionist && worker.getUserName().equals(receptionistName)) {
                // Remove from in-memory list
                workers.remove(worker);

                // Remove from the database
                deleteReceptionistFromDatabase(receptionistName);
                return;
            }
        }
        System.out.println("Receptionist not found: " + receptionistName);
    }

    //delete a reciptionistFromdataBase
    private void deleteReceptionistFromDatabase(String userName) {
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

    public void viewReceptionistDetails(String receptionistName) {
        System.out.println("List of Workers:");
        for (Receptionist worker : workers) {
            System.out.println("Name: " + worker.getUserName() + ", Job Title: " + worker.getRole() + ", Salary: " + worker.getSalary());
        }
    }


//------TESTCODEE------///
//    public static void main(String[] args) {
//        // Obtain the singleton instance of the Manager class
//        Manager manager = Manager.getInstance();
//
////        // Test adding receptionists
////        System.out.println("Testing Add Functionality:");
////        manager.addReceptionist("John", "john.doe@example.com", "0101234567", "john123", "receptionist",5000);
////        manager.addReceptionist("Jane" ,"jane.smith@example.com", "0107654321", "jane123", "receptionist",8000);
////        manager.addReceptionist("Emily" , "emily.davis@example.com", "0109876543", "emily123", "receptionist",2000);
//
//        // View the list of receptionists in the system
////        System.out.println("\nCurrent List of Receptionists:");
////        manager.viewReceptionistDetails(null);
////
////        // Test editing a receptionist's details
////        System.out.println("\nTesting Edit Functionality:");
////        manager.editReceptionist("sara", 5000, "sara1234" ,"011111111111");
////
////        // View the updated list of receptionists
////        System.out.println("\nUpdated List of Receptionists:");
////        manager.viewReceptionistDetails(null);
////
////        // Test deleting a receptionist
////        System.out.println("\nTesting Delete Functionality:");
////        manager.deleteReceptionist("John");
////
////        // View the final list of receptionists
////        System.out.println("\nFinal List of Receptionists:");
////        manager.viewReceptionistDetails(null);
//    }
//
//}
}