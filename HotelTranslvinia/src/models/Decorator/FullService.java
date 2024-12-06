package models.Decorator;

import models.Room;

//Full -> 4 * main price of room
public class FullService extends BoardingRoomDecorator {
    private final int FullServiceCost = 4 * getRoomPrice();
    public FullService(Room room) {
        super(room);
       // room.setRoomPrice(room.getRoomPrice() + ( 3 * getRoomPrice() ) ); // Add cost for Full Service
    }

    @Override
    public String getRoomType() {
        System.out.println(decoratedRoom.getRoomType() + " with Full Service");
        return decoratedRoom.getRoomType() + " with Full Service";

    }

    @Override
    public String getRoomNum() {
        return decoratedRoom.getRoomNum();
    }

    @Override
    public int getIsOccupied() {
        return decoratedRoom.getIsOccupied();
    }

    @Override
    public int getRoomPrice() {
        return decoratedRoom.getRoomPrice();
    }
    public int getTotalCost() {
        return decoratedRoom.getTotalCost()+FullServiceCost ;
    }
}
