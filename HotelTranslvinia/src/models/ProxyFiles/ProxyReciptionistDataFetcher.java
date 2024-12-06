package models.ProxyFiles;

import models.Receptionist;

import java.util.List;

public class ProxyReciptionistDataFetcher implements ReceptionistDataFetcher {

    private RealReciptionistDataFetcher realFetcher;
    private List<Receptionist> ReceptionistsList;

    public ProxyReciptionistDataFetcher() {
        realFetcher = new RealReciptionistDataFetcher();
    }

    @Override
    public List<Receptionist> fetchReceptionists() {
         ReceptionistsList = realFetcher.fetchReceptionists();
        return ReceptionistsList;
    }

    @Override
    public void addReceptionistToDatabase(Receptionist receptionist) {
        if(receptionist == null)
        {
            System.out.println("receptionist is null Retry send it");
            return;
        }
        realFetcher.addReceptionistToDatabase(receptionist);
    }

    @Override
    public void editReceptionistToDatabase(Receptionist receptionist) {
        if (receptionist == null)
        {
            System.out.println("receptionist is null Retry send it");
            return;
        }
        realFetcher.editReceptionistToDatabase(receptionist);
    }

    @Override
    public void deleteReceptionistFromDatabase(String userName) {
      if (userName == null)
      {
          System.out.println("Please enter a valid user name");
          return;
      }
      realFetcher.deleteReceptionistFromDatabase(userName);
    }
}
