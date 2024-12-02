package models.ProxyFiles;

import models.Receptionist;

import java.util.List;

public class ProxyReciptionistDataFetcher implements ReceptionistDataFetcher {

        private RealReciptionistDataFetcher realFetcher;
        private List<Receptionist> cachedReceptionists;


        @Override
        public List<Receptionist> fetchReceptionists() {
            if (cachedReceptionists == null) {
                realFetcher = new RealReciptionistDataFetcher();
                cachedReceptionists = realFetcher.fetchReceptionists();
            }
            return cachedReceptionists;
        }

}
