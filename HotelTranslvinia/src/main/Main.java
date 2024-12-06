package main;

import models.Decorator.BedAndBreakfastService;
import models.Decorator.HalfService;
import models.Factory.RoomFactory;
import models.Receptionist;
import models.Resident;
import models.Room;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        // Create a receptionist instance
        Receptionist receptionist = new Receptionist();


        // Create a resident
        Resident resident = new Resident();
        resident.setResidentName("koko");
        resident.setResidentPhone("010236589");
        resident.setDurationStay(3);
        resident.setRoomType("single");


        Resident resident2 = new Resident();
        resident2.setResidentName("mona");
        resident2.setResidentPhone("010235651");
        resident2.setDurationStay(1);
        resident2.setRoomType("single");

        // Assign a room to the resident
        receptionist.residentCheckIn(resident);
        receptionist.residentCheckIn(resident2);

        //test fetching
        receptionist.viewResidentDetails();
        //

        //test edit

       // ali -> mostafa
        receptionist.editResident(resident2, "ismail" ,"010362562112" );
        receptionist.viewResidentDetails();
        //mostafa
        // maryam

        //test Delete
        receptionist.deleteResident("koko");
        receptionist.viewResidentDetails();


    }
}