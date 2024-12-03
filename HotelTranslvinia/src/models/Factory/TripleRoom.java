package models.Factory;

import models.Room;

public class TripleRoom extends Room {
    public TripleRoom() {
        setRoomType("Triple");
        setRoomPrice(300);
        setIsOccupied(0);
    }

    @Override
    public int checkAvilability() {
        return getIsOccupied() == 0 ? 1 : 0;
    }
}
