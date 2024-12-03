package models.Decorator;

import models.Room;

public class BedAndBreakfastService extends BoardingRoomDecorator {

    public BedAndBreakfastService(Room room) {
        super(room);
        room.setRoomPrice(room.getRoomPrice() + ( 2 * getRoomPrice() )); // Add cost for B&B
    }

    @Override
    public String getRoomType() {
        return decoratedRoom.getRoomType() + " with Bed & Breakfast";
    }
}


