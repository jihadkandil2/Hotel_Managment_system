package models.ProxyFiles;

import models.Room;

import java.util.List;

public class ProxyRoomDatabase implements RoomDataBaseService{
    private RealRoomDataBaseService realRoomDatabaseService;
    private List<Room> cachedRooms;

    @Override
    public List<Room> fetchAllRooms() {
        if (cachedRooms == null) {
            System.out.println("Fetching data from the database...");
            realRoomDatabaseService = new RealRoomDataBaseService();
            cachedRooms = realRoomDatabaseService.fetchAllRooms();
        }
        System.out.println("Returning cached room data...");
        return cachedRooms;
    }

    @Override
    public void editRoom(String roomNum, String roomType, int price, int isOccupied) {
        System.out.println("Updating room data in the database...");
        if (realRoomDatabaseService == null) {
            realRoomDatabaseService = new RealRoomDataBaseService();
        }
        realRoomDatabaseService.editRoom(roomNum, roomType, price, isOccupied);

        // Invalidate cache to ensure consistency
        cachedRooms = null;
    }
}
