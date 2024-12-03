package models.Factory;

import models.ProxyFiles.ProxyRoomDatabase;
import models.ProxyFiles.RoomDataBaseService;
import models.Room;

import java.util.List;

public class RoomFactory {

    public static Room CreateRoomType(String roomType)
    {
        switch (roomType.toLowerCase()) {
            case "single":
                return new SingleRoom();
            case "double":
                return new DoubleRoom();
            case "triple":
                return new TripleRoom();
            default:
                throw new IllegalArgumentException("Invalid room type: " + roomType);
        }
    }


    public static void main(String[] args) {
        Room singleRoom = RoomFactory.CreateRoomType("single");
        System.out.println("Room Type: " + singleRoom.getRoomType());
        System.out.println("Room Price: " + singleRoom.getRoomPrice());

        Room doubleRoom = RoomFactory.CreateRoomType("double");
        System.out.println("Room Type: " + doubleRoom.getRoomType());
        System.out.println("Room Price: " + doubleRoom.getRoomPrice());



        // Create a RoomDatabaseProxy instance
        RoomDataBaseService roomProxy = new ProxyRoomDatabase();

        // Test Fetching All Rooms
        System.out.println("Fetching all rooms:");
        List<Room> rooms = roomProxy.fetchAllRooms();
        for (Room room : rooms) {
            System.out.println("Room: " + room.getRoomNum() + ", Type: " + room.getRoomType() + ", Price: " + room.getRoomPrice() + ", Occupied: " + room.getIsOccupied());
        }

//        // Test Editing a Room
//        System.out.println("\nEditing room 101...");
//        roomProxy.editRoom("103", "Triple", 250, 0);
//
//        // Fetch Updated Rooms
//        System.out.println("\nFetching updated rooms:");
//        rooms = roomProxy.fetchAllRooms();
//        for (Room room : rooms) {
//            System.out.println("Room: " + room.getRoomNum() + ", Type: " + room.getRoomType() + ", Price: " + room.getRoomPrice() + ", Occupied: " + room.getIsOccupied());
//        }
//
//        // Test Cache Behavior
//        System.out.println("\nTesting cache (no database fetch expected):");
//        rooms = roomProxy.fetchAllRooms(); // Should use the cache
    }
}
