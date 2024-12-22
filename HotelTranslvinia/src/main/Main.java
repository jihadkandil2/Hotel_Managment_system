package main;

import models.Decorator.BedAndBreakfastService;
import models.Decorator.BoardingRoomDecorator;
import models.Decorator.FullService;
import models.Decorator.HalfService;
import models.Factory.RoomFactory;
import models.Manager;
import models.Receptionist;
import models.Resident;
import models.Room;

import java.sql.Date;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
//         Create a receptionist instance
//        Receptionist receptionist = new Receptionist();
//
//        receptionist.viewResidentDetails();
//
//         Create a resident
//        Resident resident = new Resident("loay","0102265455651",2,"single","halfService");
//
//        resident.setResidentName("loay");
//        resident.setDurationStay(2);
//        resident.setRoomType("single"); //100
//        resident.setServiceType("halfService"); // 3*100 =300
//
//
//
//        receptionist.residentCheckIn(resident);
//        receptionist.viewResidentDetails();
//        totalCost 2400 omar
//
//
//        // Create a resident
//        Resident resident2 = new Resident();
//        resident2.setRoomType("triple");
//        resident2.setResidentName("ahmed");
//        resident2.setResidentPhone("0102252651");
//        resident2.setDurationStay(4);
//
//        Room room2 = roomFactory.CreateRoomType("triple");
//        decorator = new HalfService(room2);
//
//        receptionist.residentCheckIn(resident2);
//        receptionist.viewResidentDetails();
//        //ahmed Tc = 3600
//
//        // Create a resident
//        Resident resident3 = new Resident();
//        resident3.setRoomType("triple");
//        resident3.setResidentName("samia");
//        resident3.setResidentPhone("01023245251");
//        resident3.setDurationStay(2);
//
//        Room room3 = roomFactory.CreateRoomType("triple");
//        decorator = new BedAndBreakfastService(room3);
//
//        receptionist.residentCheckIn(resident3);
//        receptionist.viewResidentDetails();
//         samia tc = 1200
//
//
//        resident.setResidentName("koko");
//        resident.setResidentPhone("010236589");
//        resident.setDurationStay(3);
//        resident.setRoomType("single");
//
//
//        Resident resident2 = new Resident();
//        resident2.setResidentName("mona");
//        resident2.setResidentPhone("010235651");
//        resident2.setDurationStay(1);
//        resident2.setRoomType("single");
//
//         Assign a room to the resident
//        receptionist.residentCheckIn(resident);
//        receptionist.residentCheckIn(resident2);
//
//        test fetching
//        receptionist.viewResidentDetails();
//
//
//        test edit
//
//        ali -> mostafa
//        receptionist.editResident(resident2, "ismail" ,"010362562112" );
//        receptionist.viewResidentDetails();
//        //mostafa
//        // maryam
//
//        //test Delete
//        receptionist.deleteResident("koko");
//        receptionist.viewResidentDetails();
//
//        // Obtain the Manager instance (Singleton)
//        Manager manager = Manager.getInstance();
//
//        // Test Adding a Receptionist
//        Receptionist receptionist1 = new Receptionist();
//        receptionist1.setUserName("omar");
//        receptionist1.setPassword("omar123");
//        receptionist1.setEmail("omar@gmail.com");
//        receptionist1.setSalary(5000);
//        receptionist1.setPhone("0111234567");
//        receptionist1.setRole("receptionist");
//
//        System.out.println("Adding Receptionist:");
//        manager.addReceptionist(receptionist1);
//
//        // Test Viewing Receptionist Details
//        System.out.println("\nViewing Receptionist Details:");
//        List<Receptionist> receptionists = manager.viewReceptionistDetails(null);
//        for (Receptionist r : receptionists) {
//            System.out.println("Name: " + r.getUserName() + ", Phone: " + r.getPhone() + ", Salary: " + r.getSalary());
//        }
//
//        // Test Editing a Receptionist
//        receptionist1.setSalary(6000);
//        receptionist1.setPhone("0119876543");
//        System.out.println("\nEditing Receptionist:");
//        manager.editReceptionist(receptionist1);
//
//        // View Updated Details
//        System.out.println("\nViewing Updated Receptionist Details:");
//        receptionists = manager.viewReceptionistDetails(null);
//        for (Receptionist r : receptionists) {
//            System.out.println("Name: " + r.getUserName() + ", Phone: " + r.getPhone() + ", Salary: " + r.getSalary());
//        }
//        // omar => 6000
//
//        // Test Deleting a Receptionist
//        System.out.println("\nDeleting Receptionist:");
//        manager.deleteReceptionist("omar");
//
//        // View Details After Deletion
//        System.out.println("\nViewing Receptionist Details After Deletion:");
//        receptionists = manager.viewReceptionistDetails(null);
//        // no omar now
//        if (receptionists.isEmpty()) {
//            System.out.println("No receptionists found.");
//        } else {
//            for (Receptionist r : receptionists) {
//                System.out.println("Name: " + r.getUserName() + ", Phone: " + r.getPhone() + ", Salary: " + r.getSalary());
//            }
//        }
//
//        // Test Fetching Hotel Rooms
//        System.out.println("\nFetching Hotel Rooms:");
//        List<Room> rooms = manager.fetchHotelsRooms();
//        for (Room room : rooms) {
//            System.out.println("Room Number: " + room.getRoomNum()   + ", Occupied: " + room.getIsOccupied());
//        }

        // Get the singleton instance of Manager
        Manager manager = Manager.getInstance();

        // Test 1: Add a Receptionist
        System.out.println("\n=== Test 1: Add Receptionist ===");
        Receptionist newReceptionist = new Receptionist();
        newReceptionist.setUserName("JohnDoe");
        newReceptionist.setRole("receptionist");
        newReceptionist.setPassword("password123");
        newReceptionist.setEmail("johndoe@gmail.com");
        newReceptionist.setPhone("0123456789");
        newReceptionist.setSalary(5000);
        manager.addReceptionist(newReceptionist);

        // Test test 1: View All Receptionists
        System.out.println("\n=== Test 5: View All Receptionists ===");
        List<Receptionist> allReceptionists = manager.viewAllReciptionist();
        for (Receptionist receptionist : allReceptionists) {
            System.out.println("Name: " + receptionist.getUserName() + ", Phone: " + receptionist.getPhone() + ", Salary: " + receptionist.getSalary());
        }

        // Test 2: Edit Receptionist
        System.out.println("\n=== Test 2: Edit Receptionist ===");
        newReceptionist.setPassword("newpassword");
        newReceptionist.setPhone("0987654321");
        manager.editReceptionist(newReceptionist);

        // Test test 2: View All Receptionists
        System.out.println("\n=== Test 5: View All Receptionists ===");
         allReceptionists = manager.viewAllReciptionist();
        for (Receptionist receptionist : allReceptionists) {
            System.out.println("Name: " + receptionist.getUserName() + ", Phone: " + receptionist.getPhone() + ", Salary: " + receptionist.getSalary());
        }

        // Test 3: Delete Receptionist
        System.out.println("\n=== Test 3: Delete Receptionist ===");
        manager.deleteReceptionist("JohnDoe");

        // Test 4: View a Specific Receptionist
        System.out.println("\n=== Test 4: View Specific Receptionist ===");
        Receptionist specificReceptionist = manager.viewReceptionistDetails("JaneDoe"); // Replace "JaneDoe" with an actual username in the database
        if (specificReceptionist != null) {
            System.out.println("Name: " + specificReceptionist.getUserName());
            System.out.println("Phone: " + specificReceptionist.getPhone());
            System.out.println("Salary: " + specificReceptionist.getSalary());
        }

        // Test 5: View All Receptionists
        System.out.println("\n=== Test 5: View All Receptionists ===");
         allReceptionists = manager.viewAllReciptionist();
        for (Receptionist receptionist : allReceptionists) {
            System.out.println("Name: " + receptionist.getUserName() + ", Phone: " + receptionist.getPhone() + ", Salary: " + receptionist.getSalary());
        }

        // Test 6: Fetch Hotel Rooms
        System.out.println("\n=== Test 6: Fetch Hotel Rooms ===");
        List<Room> rooms = manager.fetchHotelsRooms();
        for (Room room : rooms) {
            System.out.println("Room Num: " + room.getRoomNum() + ", Type: " + room.getRoomType() + ", Price: " + room.getRoomPrice() + ", Occupied: " + room.getIsOccupied());
        }

        // Test 7: Track Hotel Income
        System.out.println("\n=== Test 7: Track Hotel Income ===");
        String range = "weekly"; // Can be "weekly", "monthly", or "annual"
        Date startDate = Date.valueOf("2024-1-01"); // Replace with the desired start date
        double income = manager.trackHotelIncome(range, startDate);
        System.out.println("Total Income for " + range + " starting from " + startDate + ": $" + income);


    }
}
