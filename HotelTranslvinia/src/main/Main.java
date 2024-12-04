package main;

import models.Decorator.BedAndBreakfastService;
import models.Decorator.HalfService;
import models.Factory.RoomFactory;
import models.Receptionist;
import models.Resident;
import models.Room;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        // Use the RoomFactory to create a Single Room
//        Room singleRoom = RoomFactory.CreateRoomType("single");
//
//        singleRoom.setRoomNum("101");
//        System.out.println("Original Room: " + singleRoom.getRoomType() + ", Price: " + singleRoom.getRoomPrice() + " , ID for the room: " + singleRoom.getRoomNum());
//
//
//
//        // Add Half Service to the Single Room using the decorator
//        singleRoom = new HalfService(singleRoom);
//        // Display the updated room details
//
//        System.out.println("Room Number: " + singleRoom.getRoomNum());
//        System.out.println("Room Type: " + singleRoom.getRoomType());
//        System.out.println("Room Price: " + singleRoom.getRoomPrice()); // out 200
//        System.out.println("Availability: " + (singleRoom.checkAvilability() == 0 ? "Available" : "Occupied"));
//
//        Room TripleRoom = RoomFactory.CreateRoomType("triple");
//        TripleRoom.setRoomNum("101");
//        System.out.println("Original Room: " + TripleRoom.getRoomType() + ", Price: " + TripleRoom.getRoomPrice() + " , ID for the room: " + TripleRoom.getRoomNum());
//
//
//        TripleRoom = new BedAndBreakfastService(TripleRoom);
//        System.out.println("Decorated Room: " + TripleRoom.getRoomType() + ", Price: " + TripleRoom.getRoomPrice() + " , ID for the room: " + TripleRoom.getRoomNum());


        // Create a receptionist instance
        Receptionist receptionist = new Receptionist();

        // Create a resident
        Resident resident = new Resident();
        resident.setResidentName("John Doe");
        resident.setResidentPhone("1234567890");
        resident.setDurationStay(5);

        // Assign a room to the resident
        receptionist.assignRoomToResident(resident, "single");

        // Print the assigned room details
        System.out.println("Resident: " + resident.getResidentName() +
                ", Assigned Room: " + resident.getAssignedRoom() +
                ", Total Cost: $" + resident.getTotalCost());

    }
}