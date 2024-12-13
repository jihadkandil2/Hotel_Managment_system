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

import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        // Create a receptionist instance
        Receptionist receptionist = new Receptionist();

        receptionist.viewResidentDetails();

        // Create a resident
        Resident resident = new Resident("loay","0102265455651",2,"single","halfService");

        resident.setResidentName("loay");
        resident.setResidentPhone("0102265455651");
        resident.setDurationStay(2);
        resident.setRoomType("single"); //100
        resident.setServiceType("halfService"); // 3*100 =300



        receptionist.residentCheckIn(resident);
        receptionist.viewResidentDetails();
       // totalCost 2400 omar


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
        // samia tc = 1200


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

        // Assign a room to the resident
//        receptionist.residentCheckIn(resident);
//        receptionist.residentCheckIn(resident2);

        //test fetching
//        receptionist.viewResidentDetails();
        //

        //test edit

       // ali -> mostafa
//        receptionist.editResident(resident2, "ismail" ,"010362562112" );
//        receptionist.viewResidentDetails();
//        //mostafa
//        // maryam
//
//        //test Delete
//        receptionist.deleteResident("koko");
//        receptionist.viewResidentDetails();

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




    }
}
