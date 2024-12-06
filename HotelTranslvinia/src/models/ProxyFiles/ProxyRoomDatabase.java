package models.ProxyFiles;

import models.Room;

import java.util.List;

public class ProxyRoomDatabase implements RoomDataBaseService{
    private RealRoomDataBaseService realRoomDatabaseService;
    private List<Room> cachedRooms;

    @Override
    public List<Room> fetchAllRooms() {
        if (cachedRooms == null) {
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
        realRoomDatabaseService.editRoom(roomNum, roomType, price, isOccupied);

        // Invalidate cache to ensure consistency
        cachedRooms = null;
    }
}
