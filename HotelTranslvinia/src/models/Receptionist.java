package models;

import main.Database;
import models.Decorator.BedAndBreakfastService;
import models.Decorator.BoardingRoomDecorator;
import models.Decorator.FullService;
import models.Decorator.HalfService;
import models.ProxyFiles.ProxyResidentDataFetcher;
import models.ProxyFiles.ProxyRoomDatabase;
import models.ProxyFiles.ResidentDataFetcher;

import javax.swing.*;
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
    private Room availableRoom;
    private List<Resident> residentList;
    private ResidentDataFetcher proxyFetcher;
    public Receptionist() {
        super();
        this.residentList = new ArrayList<>(); // Initialize the resident list
        this.resident = new Resident(); // Initialize the resident object
        this.proxyFetcher = new ProxyResidentDataFetcher();
    }

    public Resident getResident() {
        return resident;
    }

    public void setResident(Resident resident) {
        this.resident = resident;
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


    // Database insertion logic
    private void addResidentToDatabase(Resident resident) {
        try (Connection connection = Database.getConnection()) {
            String query = "INSERT INTO resident (name, phone, duration_stay ,total_cost,assignedRoom) VALUES (?, ?, ?, ?,?)";
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, resident.getResidentName());
            statement.setString(2, resident.getResidentPhone());
            statement.setInt(3, resident.getDurationStay());
            statement.setDouble(4, resident.getTotalCost());
            statement.setString(5,resident.getRoomType());

            statement.executeUpdate();
            System.out.println("Resident added to the database successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error adding resident to the database: " + e.getMessage());
        }
    }


    //open connection
    //set query string update on table with parameters [] where []
    // execute update statment
    public void editResident(Resident resident , String newName ,String newPhone) {
       proxyFetcher.editResidentToDatabase(resident , newName , newPhone);
    }


    // Database code for deleting a resident
    public void deleteResident(String residentName) {
        // Step 1: Check if the resident exists in the database
        Resident residentToDelete =proxyFetcher. getResidentFromDatabase(residentName);

        // Step 2: If resident doesn't exist, display a message and exit the method
        if (residentToDelete == null) {
            JOptionPane.showMessageDialog(null, "Resident not found.");
            return;
        }

        // Step 3: Get the assigned room of the resident
        Room choosedRoom = residentToDelete.getAssignedRoom();

        // Step 4: If the resident has a room, update the room status to available
        if (choosedRoom != null) {
            updateRoomStatusToAvailable(choosedRoom);
        }

        // Step 5: Remove the resident from the database
        proxyFetcher.deleteResidentFromDatabase(residentName);

        // Step 6: Display success message
        System.out.println("Resident and room status deleted successfully.");
    }




    //use proxy to fetch resident from database in a list
    // print the list content
    public String viewResidentDetails() {
        residentList = proxyFetcher.fetchResidents();

        StringBuilder residentsData = new StringBuilder();
        for (Resident resident : residentList) {
            residentsData.append("Name: ").append(resident.getResidentName()).append("\n");
            residentsData.append("Phone: ").append(resident.getResidentPhone()).append("\n");
            residentsData.append("Duration of Stay: ").append(resident.getDurationStay()).append("\n");
            residentsData.append("Total Cost: $").append(resident.getTotalCost()).append("\n");
            residentsData.append("Room Type: ").append(resident.getRoomType()).append("\n");
            residentsData.append("-----------------------------\n");
        }

        return residentsData.toString();
    }

    //giving a room and duration stay we multiply cost with number of nights
    private int CalcTotalCostWithout_Service(Room room  , int durationStay ) {
        if (room == null) {
            throw new IllegalArgumentException("Room type cannot be null");
        }

        int totalCost = room.getTotalCost() * durationStay  ;
        System.out.println("Total Cost: $" + totalCost);
        return totalCost;
    }

    //giving a room and duration stay and service we multiply cost(room+service) with number of nights
    private int CalcTotalCostWith_Service(Resident resident) {
        int totalCost =0 ;
        BoardingRoomDecorator decorator ;
        if (resident.getServiceType().equalsIgnoreCase("FullService") )
        {
            decorator = new FullService(availableRoom);
        }
        else if(resident.getServiceType().equalsIgnoreCase("HalfService")){
            decorator = new HalfService(availableRoom);
        }
        else {
            decorator = new BedAndBreakfastService(availableRoom);
        }
        totalCost = decorator.getTotalCost() * resident.getDurationStay();
        return totalCost;
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


    //DATABASE CODE handeled by proxy [true]
    private void makeRoomOccubied(Room choosedRoom)
    {
        ProxyRoomDatabase proxyRoomDatabase = new ProxyRoomDatabase();
        proxyRoomDatabase.editRoom(choosedRoom.getRoomNum(), choosedRoom.getRoomType(), choosedRoom.getRoomPrice(), 1);
        System.out.println("Room : " + choosedRoom.getRoomNum() + " is occubied now" );
    }
    private void updateRoomStatusToAvailable(Room choosedRoom) {

        if (choosedRoom != null) {
            ProxyRoomDatabase proxyRoomDatabase = new ProxyRoomDatabase();
            proxyRoomDatabase.editRoom(choosedRoom.getRoomNum(), choosedRoom.getRoomType(), 0, 0);
            System.out.println("Room " + choosedRoom.getRoomNum() + " is now available.");
        } else {
            System.out.println("Room not found, cannot update status.");
        }
    }

    //[0] i have a resident with its info (name ,phone, roomtype , durationstay)
    //[1] check available room of matched type
    //[2] if no matches say sorry
    //[3] if matched take first one
    //[4] room must be occupied now
    //[5] check if (!service) =>  calcTotalCostWithoutDecoration
    //[6] if (service) =>  calcCostWithDecorated
    //[7] add him in the system (database)

    public void residentCheckIn(Resident resident) {
        // Step 1: Check for available rooms of the requested type
        List<Room> availableRooms = checkAvailableRoom(resident.getRoomType());

        // Step 2: If no rooms are available, display a message and exit the method
        if (availableRooms == null || availableRooms.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No available rooms of type " + resident.getRoomType() + ".");
            return;
        }

        // Step 3: Assign the first available room (this can be modified for custom selection)
        availableRoom = availableRooms.get(0);
        makeRoomOccubied(availableRoom);

        // Step 4: Calculate the total cost based on the service type
        int totalCost = 0;
        if (resident.getServiceType() == null) {
            totalCost = CalcTotalCostWithout_Service(availableRoom, resident.getDurationStay());
        } else {
            totalCost = CalcTotalCostWith_Service(resident);
        }

        // Step 5: Set the total cost for the resident and add them to the database
        resident.setTotalCost(totalCost);
        addResidentToDatabase(resident);
        System.out.println("Room assigned successfully!");
    }

}
