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
    public void editRoom(Room room) {
        if (realRoomDatabaseService == null) {
            realRoomDatabaseService = new RealRoomDataBaseService();
        }
        realRoomDatabaseService.editRoom(room);

        // Invalidate cache to ensure consistency
        cachedRooms = null;
        System.out.println("Cache invalidated, room status updated.");
    }
}
