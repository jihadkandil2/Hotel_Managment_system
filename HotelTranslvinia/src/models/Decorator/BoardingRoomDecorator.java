package models.Decorator;

import models.Room;

//Full -> 3 * main price of room
//Half -> 1 * main price
// b&B -> 2 * main price
public abstract class BoardingRoomDecorator extends Room {
    protected Room decoratedRoom; // Reference to the room being decorated

    public BoardingRoomDecorator(Room room) {
        this.decoratedRoom = room;
    }

    @Override
    public int checkAvilability() {
        return decoratedRoom.getIsOccupied();
    }
}
