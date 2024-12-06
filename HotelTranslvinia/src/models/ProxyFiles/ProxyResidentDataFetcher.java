package models.ProxyFiles;

import models.Resident;

import java.util.List;

public class ProxyResidentDataFetcher implements ResidentDataFetcher {
    private RealResidentDataFetcher realFetcher;
    private List<Resident> ResidentsList;
    //private String name;
    public ProxyResidentDataFetcher() {
      //  this.name = residentName;
        this.realFetcher = new RealResidentDataFetcher();
    }
    @Override
    public List<Resident> fetchResidents() {
        ResidentsList = realFetcher.fetchResidents();
        return ResidentsList;
    }

    @Override
    public void editResidentToDatabase(Resident resident , String newName ,String newPhone) {
         if (resident == null) {
             System.out.println("Please enter a valid resident to edit");
             return;
         }
         realFetcher.editResidentToDatabase(resident , newName ,newPhone);
    }

    @Override
    public void deleteResidentFromDatabase(String residentName) {
       if (residentName == null) {
           System.out.println("Please enter a valid resident to delete");
           return;
       }
       realFetcher.deleteResidentFromDatabase(residentName);
    }
}
