package models.ProxyFiles;

import models.Resident;

import java.util.List;

public class ProxyResidentDataFetcher implements ResidentDataFetcher {
    private RealResidentDataFetcher realFetcher;
    private List<Resident> cachedResidents;
    //private String name;
    public ProxyResidentDataFetcher() {
      //  this.name = residentName;
        this.realFetcher = new RealResidentDataFetcher();
    }
    @Override
    public List<Resident> fetchResidents() {
        if (cachedResidents == null) {
           // realFetcher = new RealResidentDataFetcher();
            cachedResidents = realFetcher.fetchResidents();
        }
        return cachedResidents;
    }
}
