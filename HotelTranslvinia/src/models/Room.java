package models;

public abstract class Room {
    private String roomNum ;
    private String roomType;
    private int roomPrice;
    private int isOccupied;
    private Resident assignedResident;

    public Room() {}
    public abstract int checkAvilability();


    public Resident getAssignedResident() {
        return assignedResident;
    }

    public void setAssignedResident(Resident assignedResident) {
        this.assignedResident = assignedResident;
    }

    public String getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(String roomNum) {
        this.roomNum = roomNum;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {

        this.roomType = roomType;
    }

    public int getRoomPrice() {
        return roomPrice;
    }

    public void setRoomPrice(int roomPrice) {
        this.roomPrice = roomPrice;
    }

    public int getIsOccupied() {
        return isOccupied;
    }

    public void setIsOccupied(int isOccupied) {
        this.isOccupied = isOccupied;
    }

    public int getTotalCost() {
        return roomPrice;
    }

}