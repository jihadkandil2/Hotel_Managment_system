package models.ProxyFiles;

import models.Room;

import java.util.List;

public interface RoomDataBaseService {
    List<Room> fetchAllRooms();       // Fetch all room data
    void editRoom(Room roomToEdit); // Edit room data
}
