package models;

/**
 * obj Resident -> add , edit , delete , viewdetails [name ,stayDuration ,totalcost() ,contact_info]
 * assign room based on availability
 * obj Room ->
 * calcCost basedon Room type , nights ,boarding option
 * */
public class Receptionist extends User{

    private int salary;
    private String phone;
    private Resident residentObj;
    private Room roomObj;

    // Getters and Setters
    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

}
