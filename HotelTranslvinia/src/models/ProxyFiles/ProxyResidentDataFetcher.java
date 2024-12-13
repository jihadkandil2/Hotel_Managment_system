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
public Resident getResidentFromDatabase(String residentName){
    if (residentName == null || residentName.isEmpty()) {
        System.out.println("Resident name cannot be null or empty.");
        return null;
    }
    // استرجاع المقيم من قاعدة البيانات عبر RealResidentDataFetcher
    return realFetcher.getResidentFromDatabase(residentName);
}
    @Override

    public void editResidentToDatabase(Resident resident, String newName, String newPhone) {
        if (resident == null) {
            System.out.println("Resident not found.");
            return;
        }
        // إجراء التعديل في قاعدة البيانات عبر `RealResidentDataFetcher`
        realFetcher.editResidentToDatabase(resident, newName, newPhone);
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
