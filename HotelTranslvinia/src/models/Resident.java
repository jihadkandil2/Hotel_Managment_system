package models;

public class Resident {
    private String residentName;
    private String residentPhone;
    private int DurationStay;
    private double TotalCost ;
    private String roomType;
    private String ServiceType ;
    //link var (service type)
    //totalCost

    private Room assignedRoom;  // الغرفة المعينة للمقيم

    // Getter and Setter for assignedRoom
    public Room getAssignedRoom() {
        return assignedRoom;
    }

    public void setAssignedRoom(Room assignedRoom) {
        this.assignedRoom = assignedRoom;
    }
    public Resident() {

    }

    public Resident(String residentName, String residentPhone, int durationStay, String ServiceType, String roomType) {
        this.residentName = residentName;
        this.residentPhone = residentPhone;
        this. DurationStay = durationStay;
        this.roomType = roomType;
        this.ServiceType = ServiceType;
    }

    public String getServiceType() {
        return ServiceType;
    }

    public void setServiceType(String serviceTyoe) {
        ServiceType = serviceTyoe;
    }

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

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String assignedRoom) {
        this.roomType = assignedRoom;
    }
}
