package models.ProxyFiles;

import main.Database;
import models.Factory.RoomFactory;
import models.Resident;
import models.Room;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RealRoomDataBaseService implements RoomDataBaseService {
    @Override
    public List<Room> fetchAllRooms() {
        List<Room> rooms = new ArrayList<>();
        RoomFactory roomFactory = new RoomFactory();

        String query = "SELECT * FROM room";

        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String roomNum = rs.getString("room_num");
                String roomType = rs.getString("room_type");
                int roomPrice = rs.getInt("room_price");
                int isOccupied = rs.getInt("is_occupied");
                String residentName = rs.getString("residentName");

                Room room = roomFactory.CreateRoomType(roomType);
                room.setRoomNum(roomNum);
                room.setIsOccupied(isOccupied);
                Resident myresident = new Resident();
                myresident.setResidentName(residentName);
                myresident.setAssignedRoom(room);
                room.setAssignedResident(myresident);
                rooms.add(room);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rooms;
    }

    @Override
    public void editRoom(Room room) {
        String query = "UPDATE room SET  is_occupied = ?, residentName=? WHERE room_num = ?";

        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, room.getIsOccupied());
            if (room.getIsOccupied() == 0) {
                stmt.setString(2, null);
            }
            else {
                stmt.setString(2, room.getAssignedResident().getResidentName());
            }
            stmt.setString(3, room.getRoomNum());

            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Room " +  room.getRoomNum() + " updated successfully in the database.");
            } else {
                System.out.println("Room " +  room.getRoomNum() + " not found in the database.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
