package models.ProxyFiles;

import models.Room;

import java.util.List;

public class ProxyRoomDatabase implements RoomDataBaseService {
    private RealRoomDataBaseService realRoomDatabaseService;
    private List<Room> cachedRooms;

    @Override
    public List<Room> fetchAllRooms() {
        if (cachedRooms == null) {
            System.out.println("Fetching rooms from real database...");
            realRoomDatabaseService = new RealRoomDataBaseService();
            cachedRooms = realRoomDatabaseService.fetchAllRooms();
        }
        return cachedRooms;
    }

    @Override
    public void editRoom(String roomNum, String roomType, int price, int isOccupied) {
        if (realRoomDatabaseService == null) {
            realRoomDatabaseService = new RealRoomDataBaseService();
        }
        System.out.println("Editing room: " + roomNum + ", Occupied status: " + isOccupied);
        realRoomDatabaseService.editRoom(roomNum, roomType, price, isOccupied);

        // Invalidate cache to ensure consistency
        cachedRooms = null;
        System.out.println("Cache invalidated, room status updated.");
    }
}
