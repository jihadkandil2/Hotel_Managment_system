package models;

import java.util.ArrayList;
import java.util.List;
import main.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import models.ProxyFiles.ProxyReciptionistDataFetcher;
import models.ProxyFiles.ProxyRoomDatabase;
import models.ProxyFiles.ReceptionistDataFetcher;
import models.ProxyFiles.RoomDataBaseService;


public class Manager extends User {
    Receptionist receptionist;
    Resident resident;
    private List<Receptionist> workers;
    private ReceptionistDataFetcher Proxyfetcher ;
    private RoomDataBaseService roomFetcher;
    private List<Room> roomList;


    private Manager() {
        super(); // Set default username and password
        //workers = fetcher.fetchReceptionists();
    }

    //Singlton pattern to manage only one instance of manager can be created
    private static Manager instance;
    public static Manager getInstance() {
        if (instance == null) {
            instance = new Manager();
        }
        return instance;
    }



    //Logic steps:-
    //-------------
    //[1] add button clicked trigger fun -> addReceptionist
    //[2] addReceptionist -> addReceptionistToDatabase (proxy)
    //[3] trigger -> addReceptionistToDatabase(RealReciptionistDataFetcher)
    //[4] last function will add him
    public void addReceptionist(Receptionist receptionist) {
        Proxyfetcher = new ProxyReciptionistDataFetcher();
        Proxyfetcher.addReceptionistToDatabase(receptionist);
    }

    //Logic steps:-
    //-------------
    //[1] click on edit button -> open ui interface of that receptionist worker
    //[2] put in the input field new data (salary , pass , phone)
    //[3] that will be put in an object to be sent to data base
    //[4] click submit button it calls -> editReceptionist(manager file)
    //[5] call -> editReceptionistToDatabase (Proxy file)
    //[6] call -> editReceptionistToDatabase (RealdataFetcher file) which will handle edit in the data base
    public void editReceptionist(Receptionist receptionist) {
        Proxyfetcher = new ProxyReciptionistDataFetcher();
        Proxyfetcher.editReceptionistToDatabase(receptionist);
    }



    //Logic steps:-
    //-------------
    //[1] delete button clicked call ->deleteReceptionist (manager.file)
    //[2] calls - > deleteReceptionistFromDatabase(proxy file ) then it calls that in (Real file )
    //[3] which take a username of a worker and delete it
    public void deleteReceptionist(String userName) {
        Proxyfetcher = new ProxyReciptionistDataFetcher();
        Proxyfetcher.deleteReceptionistFromDatabase(userName);
    }

    //Logic steps:-
    //-------------
    //[1] click on show details
    //[2] calls viewReceptionistDetails (manager file) => calls FetchReceptionist (proxy file ) then from (realProxy)
    //[3] it will get all receptionist from data base and show it in a list
    public List<Receptionist> viewReceptionistDetails(String receptionistName) {
        Proxyfetcher = new ProxyReciptionistDataFetcher();
        workers= new ArrayList<>();
        workers =Proxyfetcher.fetchReceptionists();
        return workers ;
    }

    //Logic steps:-
    //-------------
    //[1]when manager ui load it calls fetchHotelsRooms (manager file) => calls FetchAllRooms (proxy file)
    //[2]then calls = > fetchAllRooms (RealFetcher file)
    //[3] return a list of romm with all its data then i will filter to only take isOccubied attribute
    public List<Room> fetchHotelsRooms()
    {
        roomFetcher =new ProxyRoomDatabase();
        roomList = new ArrayList<>();
        roomList =roomFetcher.fetchAllRooms() ;
        return roomList;
    }

}