package models;

import main.Database;
import models.ProxyFiles.ProxyResidentDataFetcher;
import models.ProxyFiles.ProxyRoomDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    //DataBase CODE
    private List<Room> checkAvailableRoom(String roomType) {
        ProxyRoomDatabase proxyRoomDatabase = new ProxyRoomDatabase();

        // Step 1: Fetch all rooms using the proxy
        List<Room> availableRooms = proxyRoomDatabase.fetchAllRooms();

        // Step 2: Loop through the list and filter based on room type and availability (i.e., is_occupied = 0)
        availableRooms.removeIf(room -> room.getIsOccupied() == 1 || !room.getRoomType().equalsIgnoreCase(roomType));

        // Step 3: Check if we have any available rooms
        if (availableRooms.isEmpty()) {
            System.out.println("No available rooms of type " + roomType + " to assign.");
            return null;
        }

        return availableRooms;
    }

    //DATABASE CODE
    private void makeOccubied(Room choosedRoom)
    {
        ProxyRoomDatabase proxyRoomDatabase = new ProxyRoomDatabase();
        proxyRoomDatabase.editRoom(choosedRoom.getRoomNum(), choosedRoom.getRoomType(), choosedRoom.getRoomPrice(), 1);
        System.out.println("Room : " + choosedRoom.getRoomNum() + " is occubied now" );
    }

    public void assignRoomToResident(Resident resident , String roomType) {
        // step check available one
        List<Room> availableRooms = checkAvailableRoom(roomType);
        if (availableRooms.isEmpty()) {
            return;
        }

        // Assign the first available room (can be modified for custom selection)
        Room availableRoom = availableRooms.get(0);

        System.out.println("Assigning room " + availableRoom.getRoomNum() + " to resident " + resident.getResidentName());
        resident.setAssignedRoom(availableRoom.getRoomNum());

        // Step 3: Update the room as occupied using the proxy
        makeOccubied(availableRoom);

        // Step 4: Calculate and update total cost
        int totalCost = availableRoom.getRoomPrice() * resident.getDurationStay();
        resident.setTotalCost(totalCost);

        System.out.println("Room assigned successfully!");
        System.out.println("Updated Total Cost: $" + totalCost);
    }

    // Fetch an available room from the database
    private Room fetchAvailableRoom() {
        String fetchQuery = "SELECT * FROM room WHERE is_occupied = 0 LIMIT 1";
        try (Connection connection = Database.getConnection();
             PreparedStatement fetchStmt = connection.prepareStatement(fetchQuery)) {

            ResultSet rs = fetchStmt.executeQuery();
            if (rs.next()) {
                Room room = new Room() {
                    @Override
                    public int checkAvilability() {
                        return 0;
                    }
                };
                room.setRoomNum(rs.getString("room_num"));
                room.setRoomType(rs.getString("room_type"));
                room.setRoomPrice(rs.getInt("room_price"));
                room.setIsOccupied(rs.getInt("is_occupied"));
                return room;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error while fetching available room: " + e.getMessage());
        }
        return null; // No available room
    }

    // Mark a room as occupied in the database
    private void markRoomAsOccupied(String roomNum) {
        String updateQuery = "UPDATE room SET is_occupied = 1 WHERE room_num = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {

            updateStmt.setString(1, roomNum);
            int rowsUpdated = updateStmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Room " + roomNum + " marked as occupied in the database.");
            } else {
                System.err.println("Failed to mark room " + roomNum + " as occupied.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error while marking room as occupied: " + e.getMessage());
        }
    }


}
