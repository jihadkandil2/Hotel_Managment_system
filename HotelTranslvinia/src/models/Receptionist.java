package models;

import main.Database;
import models.Decorator.BedAndBreakfastService;
import models.Decorator.BoardingRoomDecorator;
import models.Decorator.FullService;
import models.Decorator.HalfService;
import models.ProxyFiles.ProxyResidentDataFetcher;
import models.ProxyFiles.ProxyRoomDatabase;
import models.ProxyFiles.ResidentDataFetcher;
import models.ProxyFiles.RoomDataBaseService;

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
    private Room availableRoom;
    private List<Resident> residentList;
    private ResidentDataFetcher proxyResidentFetcher;
    private RoomDataBaseService proxyRoomDatabase ;
    public Receptionist() {
        super();
        this.residentList = new ArrayList<>(); // Initialize the resident list
        this.proxyResidentFetcher = new ProxyResidentDataFetcher();
        this.proxyRoomDatabase = new ProxyRoomDatabase();
    }



    // Getters and Setters:
    //---------------------
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

    //---------------------------------------------------------------------------------------------------------------------

    //Helper functions:
    //-----------------
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
        if(choosedRoom == null)
        {
            System.out.println("Please enter Room and try again..");
            return;
        }
        proxyRoomDatabase.editRoom(choosedRoom);
        System.out.println("Room : " + choosedRoom.getRoomNum() + " is occubied now" );
    }

    //
    private void makeRoomAvailable(Room room)
    {
        if(room == null)
        {
            System.out.println("Please enter Room and try again..");
            return;
        }
        proxyRoomDatabase.editRoom(room);
        System.out.println("Room : " + room.getRoomNum() + " is Available now" );
    }

    // Database insertion logic
    private void addResidentToDatabase(Resident resident) {
        if (resident == null)
        {
            System.out.println("resident field is empty Please send Resident to add...");
            return ;
        }
        proxyResidentFetcher.addResidentToDatabase(resident);
    }




    //---------------------------------------------------------------------------------------------------------------------

    //main functions: [edit - delete -view - checkin]
    //-----------------------------------------------
    //DataBase Code for edit a resident
    public void editResident(Resident resident , String newName ,String newPhone) {
        if (resident == null)
        {
            System.out.println("resident field is empty Please send Resident to edit...");
            return ;
        }
        proxyResidentFetcher.editResidentToDatabase(resident , newName , newPhone);
    }

    // Database code for deleting a resident
    public void deleteResident(String residentName) {
        // Step 1: Check if the resident exists in the database
        Resident residentToDelete =proxyResidentFetcher.getResidentFromDatabase(residentName);

        // Step 2: If resident doesn't exist, display a message and exit the method
        if (residentToDelete == null) {
            JOptionPane.showMessageDialog(null, "Resident not found.");
            return;
        }

        // Step 3: Get the assigned room of the resident
        Room choosedRoom = residentToDelete.getAssignedRoom();


        if (choosedRoom == null) {
            System.out.println("That resident does not have room assigned.");
            JOptionPane.showMessageDialog(null, "This resident has no such room");
            return;
        }

        // Step 4: If the resident has a room, update the room status to available
        choosedRoom.setIsOccupied(0);
        choosedRoom.setAssignedResident(null);
        makeRoomAvailable(choosedRoom);
        // Step 5: Remove the resident from the database
        proxyResidentFetcher.deleteResidentFromDatabase(residentName);
    }

    //use proxy to fetch resident from database in a list
    // print the list content
    public String viewResidentDetails() {
        residentList = proxyResidentFetcher.fetchResidents();
        if (residentList == null) {
            //write code for display message in gui
            return null;
        }
        StringBuilder residentsData = new StringBuilder();
        for (Resident resident : residentList) {
            residentsData.append("Name: ").append(resident.getResidentName()).append("\n");
            residentsData.append("Phone: ").append(resident.getResidentPhone()).append("\n");
            residentsData.append("Duration of Stay: ").append(resident.getDurationStay()).append("\n");
            residentsData.append("Total Cost: $").append(resident.getTotalCost()).append("\n");
            residentsData.append("Room Type: ").append(resident.getAssignedRoom().getRoomType()).append("\n");
            residentsData.append("Room Number: ").append(resident.getAssignedRoom().getRoomNum()).append("\n");
            residentsData.append("-----------------------------\n");
        }

        return residentsData.toString();
    }

    //Logic Steps:-
    //------------
    //[0] i have a resident with its info (name ,phone, roomtype , durationstay)
    //[1] check available room of matched type
    //[2] if no matches say sorry
    //[3] if matched take first one
    //[4] room must be occupied now
    //[5] make it assigned into that resident
    //[5] check if (!service) =>  calcTotalCostWithoutDecoration
    //[6] if (service) =>  calcCostWithDecorated
    //[7] add him in the system (database)
    public void residentCheckIn(Resident resident) {
        // Step 1: Check for available rooms of the requested type
        List<Room> availableRooms = checkAvailableRoom(resident.getAssignedRoom().getRoomType());

        // Step 2: If no rooms are available, display a message and exit the method
        if (availableRooms == null || availableRooms.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No available rooms of type " + resident.getAssignedRoom().getRoomType() + ".");
            return;
        }

        // Step 3: Assign the first available room (this can be modified for custom selection)
        availableRoom = availableRooms.getFirst();
        availableRoom.setIsOccupied(1);
        availableRoom.setAssignedResident(resident);

        System.out.println(availableRoom.getAssignedResident().getResidentName());
        //DEPUG---------------
        System.out.println("Room : " + availableRoom.getRoomNum() + " is occupied now" );
       // System.out.println("Room Resident: " + availableRoom.getAssignedResident() );

        resident.setAssignedRoom(availableRoom);
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
        //DEPUG:---------------------------------------------------------------------------
        System.out.println("Resident name = " + resident.getResidentName());
        System.out.println("Resident phone number = " + resident.getResidentPhone());
        System.out.println("Resident duration = " + resident.getDurationStay());
        System.out.println("Total cost = " + totalCost);
        System.out.println("Room type = " + resident.getAssignedRoom().getRoomType());
        System.out.println("Room number = " + resident.getAssignedRoom().getRoomNum());
        //----------------------------------------------------------------------------------above delete
        addResidentToDatabase(resident);
        System.out.println("Room assigned successfully!");
    }

    //-----------------------------------------------------------------------------------------------------------------
}
