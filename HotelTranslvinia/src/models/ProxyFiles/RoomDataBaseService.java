package models.ProxyFiles;

import models.Room;

import java.util.List;

public interface RoomDataBaseService {
    List<Room> fetchAllRooms();       // Fetch all room data
    void editRoom(String roomNum, String roomType, int price, int isOccupied); // Edit room data
}
