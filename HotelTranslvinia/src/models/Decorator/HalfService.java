package models.Decorator;

import models.Room;


//Half -> 1 * main price
public class HalfService extends BoardingRoomDecorator{
    public HalfService(Room room) {
        super(room);
        room.setRoomPrice(room.getRoomPrice() + (  getRoomPrice() )); // Add cost for Half Service
    }

    @Override
    public String getRoomType() {
        return decoratedRoom.getRoomType() + " with Half Service";
    }

}
