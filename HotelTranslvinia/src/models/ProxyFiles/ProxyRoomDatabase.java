package models.ProxyFiles;

import models.Room;

import java.util.List;

public class ProxyRoomDatabase implements RoomDataBaseService {
    private RealRoomDataBaseService realRoomDatabaseService;
    private List<Room> Rooms;

    @Override
    public List<Room> fetchAllRooms() {

        realRoomDatabaseService = new RealRoomDataBaseService();
        Rooms = realRoomDatabaseService.fetchAllRooms();
        return Rooms;
    }

    @Override
    public void editRoom(Room room) {
        if (realRoomDatabaseService == null) {
            realRoomDatabaseService = new RealRoomDataBaseService();
        }
        realRoomDatabaseService.editRoom(room);

        // Invalidate cache to ensure consistency
        Rooms = null;
        System.out.println("Cache invalidated, room status updated.");
    }
}
