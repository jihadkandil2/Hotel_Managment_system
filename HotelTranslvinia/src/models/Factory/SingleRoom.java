package models.Factory;

import models.Room;

public class SingleRoom extends Room {
    public SingleRoom() {
        setRoomType("Single");
        setRoomPrice(100);
        setIsOccupied(0);
    }

    @Override
    public int checkAvilability() {

        return getIsOccupied() == 0 ? 1 : 0;
    }
}
