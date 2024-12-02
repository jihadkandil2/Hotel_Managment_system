package models.ProxyFiles;
import models.Resident;
import java.util.List;

public interface ResidentDataFetcher {
    List<Resident> fetchResidents();
}
