package models.ProxyFiles;

import java.sql.Date;

public class ProxyIncomeDataFetcher implements IncomeDataFetcher{
    private RealIncomeDataFetcher realIncomeDataFetcher;

    @Override
    public double fetchIncome(String range, Date startDate) {
        // Lazily initialize the real data fetcher
        if (realIncomeDataFetcher == null) {
            realIncomeDataFetcher = new RealIncomeDataFetcher();
        }

        System.out.println("Fetching income for range " + range + "and date " + startDate);
        return realIncomeDataFetcher.fetchIncome(range ,startDate);
    }
}
