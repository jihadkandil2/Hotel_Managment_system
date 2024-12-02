package models.ProxyFiles;
import models.Receptionist;

import java.util.List;


public interface ReceptionistDataFetcher {
    List<Receptionist> fetchReceptionists();
}
