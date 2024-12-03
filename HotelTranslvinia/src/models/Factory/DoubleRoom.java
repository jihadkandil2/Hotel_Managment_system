package models.Factory;

import models.Room;

public class DoubleRoom extends Room {
    public DoubleRoom() {
        setRoomType("Double");
        setRoomPrice(200);
        setIsOccupied(0);
    }

    @Override
    public int checkAvilability() {
        return getIsOccupied() == 0 ? 1 : 0;
    }
}
