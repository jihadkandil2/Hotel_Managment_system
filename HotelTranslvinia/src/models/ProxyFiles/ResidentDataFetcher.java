package models.ProxyFiles;
import models.Resident;
import java.util.List;

public interface ResidentDataFetcher {
    List<Resident> fetchResidents();
    public void editResidentToDatabase(Resident resident,  String newName ,String newPhone);
    public void deleteResidentFromDatabase(String residentName);
    public Resident getResidentFromDatabase(String residentName);
}
