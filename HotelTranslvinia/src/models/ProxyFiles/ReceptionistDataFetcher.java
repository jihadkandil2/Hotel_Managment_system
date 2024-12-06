package models.ProxyFiles;
import models.Receptionist;

import java.util.List;


public interface ReceptionistDataFetcher {
    List<Receptionist> fetchReceptionists();
    void addReceptionistToDatabase(Receptionist receptionist) ;
    void editReceptionistToDatabase(Receptionist receptionist) ;
    void deleteReceptionistFromDatabase(String userName) ;

}
