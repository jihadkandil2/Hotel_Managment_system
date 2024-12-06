package models;

import main.Database;
import models.Decorator.BedAndBreakfastService;
import models.Decorator.BoardingRoomDecorator;
import models.Decorator.FullService;
import models.Decorator.HalfService;
import models.Factory.RoomFactory;
import models.Factory.SingleRoom;
import models.ProxyFiles.ProxyResidentDataFetcher;
import models.ProxyFiles.ProxyRoomDatabase;
import models.ProxyFiles.ResidentDataFetcher;

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
    private Room availableRoom;
    private List<Resident> residentList;
    private ResidentDataFetcher proxyFetcher;
    public Receptionist() {
        super();
        this.residentList = new ArrayList<>(); // Initialize the resident list
        this.resident = new Resident(); // Initialize the resident object
        this.proxyFetcher = new ProxyResidentDataFetcher();
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
        proxyFetcher.deleteResidentFromDatabase(residentName);
    }

    //use proxy to fetch resident from database in a list
    // print the list content
    public void viewResidentDetails() {
        residentList =proxyFetcher.fetchResidents();

        System.out.println("List of Residents:");
        for (Resident resident : residentList) {
            System.out.println("Name: " + resident.getResidentName() +
                    ", Phone: " +
                    resident.getResidentPhone() +
                    ", Duration of Stay: " +
                    resident.getDurationStay() +
                    ", Total Cost: $" +
                    resident.getTotalCost() +
                    ", Room = " + resident.getRoomType() );
        }
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

    //[0] i have a resident with its info (name ,phone, roomtype , durationstay)
    //[1] check available room of matched type
    //[2] if no matches say sorry
    //[3] if matched take first one
    //[4] room must be occupied now
    //[5] check if (!service) =>  calcTotalCostWithoutDecoration
    //[6] if (service) =>  calcCostWithDecorated
    //[7] add him in the system (database)

    public void residentCheckIn(Resident resident ) {
        // step check available one
        List<Room> availableRooms = checkAvailableRoom(resident.getRoomType());
        if (availableRooms == null) {
            return;
        }

        // Assign the first available room (can be modified for custom selection)
        availableRoom = availableRooms.get(0);
        makeRoomOccubied(availableRoom);

        //check on service
        int totalCost =0;
        if (resident.getServiceType() ==null)
        {
             totalCost = CalcTotalCostWithout_Service(availableRoom , resident.getDurationStay() ) ;
        }
        else {
            totalCost = CalcTotalCostWith_Service(resident);
        }

        resident.setTotalCost(totalCost);
        addResidentToDatabase(resident);
        System.out.println("Room assigned successfully!");

    }
}
