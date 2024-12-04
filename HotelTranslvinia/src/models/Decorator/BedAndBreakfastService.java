package models.Decorator;

import models.Room;

public class BedAndBreakfastService extends BoardingRoomDecorator {
    private final int breakfastCost = 2 * getRoomPrice(); // Fixed cost for Bed and Breakfast
    public BedAndBreakfastService(Room room) {
        super(room);
       // room.setRoomPrice(room.getRoomPrice() + ( 2 * getRoomPrice() )); // Add cost for B&B
    }

    @Override
    public String getRoomType() {
        return decoratedRoom.getRoomType() + " with Bed & Breakfast";
    }

    @Override
    public String getRoomNum() {
        return decoratedRoom.getRoomNum();
    }

    @Override
    public int getRoomPrice() {
        return decoratedRoom.getRoomPrice();
    }

    @Override
    public int getIsOccupied() {
        return decoratedRoom.getIsOccupied();
    }
    public int getTotalCost() {
        return decoratedRoom.getTotalCost() + breakfastCost; // Add B&B cost to the decorated room's total
    }
}


