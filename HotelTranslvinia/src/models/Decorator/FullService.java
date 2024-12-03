package models.Decorator;

import models.Room;

//Full -> 2 * main price of room
public class FullService extends BoardingRoomDecorator {
    public FullService(Room room) {
        super(room);
        room.setRoomPrice(room.getRoomPrice() + ( 3 * getRoomPrice() ) ); // Add cost for Full Service
    }

    @Override
    public String getRoomType() {
        System.out.println(decoratedRoom.getRoomType() + " with Full Service");
        return decoratedRoom.getRoomType() + " with Full Service";

    }

}
