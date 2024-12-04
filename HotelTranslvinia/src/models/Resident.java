package models;

public class Resident {
    private String residentName;
    private String residentPhone;
    private int DurationStay;
    private double TotalCost ;
    private String assignedRoom;

    public int getDurationStay() {
        return DurationStay;
    }

    public void setDurationStay(int durationStay) {
        DurationStay = durationStay;
    }

    public String getResidentName() {
        return residentName;
    }

    public void setResidentName(String residentName) {
        this.residentName = residentName;
    }

    public String getResidentPhone() {
        return residentPhone;
    }

    public void setResidentPhone(String residentPhone) {
        this.residentPhone = residentPhone;
    }

    public double getTotalCost() {
        return TotalCost;
    }

    public void setTotalCost(double totalCost) {
        TotalCost = totalCost;
    }

    public String getAssignedRoom() {
        return assignedRoom;
    }

    public void setAssignedRoom(String assignedRoom) {
        this.assignedRoom = assignedRoom;
    }
}
