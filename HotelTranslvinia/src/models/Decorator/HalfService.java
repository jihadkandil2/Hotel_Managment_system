package models.Decorator;

import models.Room;


//Half -> 1 * main price
public class HalfService extends BoardingRoomDecorator{

    public HalfService(Room room) {
        super(room);
        decoratedRoom.setRoomPrice(2 * (decoratedRoom.getRoomPrice()) ); // Add cost for Half Service
    }


    @Override
    public String getRoomType() {
        return decoratedRoom.getRoomType() + " with Half Service";
    }

    @Override
    public int getRoomPrice() {
        return decoratedRoom.getRoomPrice();
    }

    @Override
    public String getRoomNum() {
        return decoratedRoom.getRoomNum();
    }

    @Override
    public int getIsOccupied() {
        return decoratedRoom.getIsOccupied();
    }
}
