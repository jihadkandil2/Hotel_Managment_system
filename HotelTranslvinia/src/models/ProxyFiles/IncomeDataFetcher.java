package models.ProxyFiles;

import java.sql.Date;

public interface IncomeDataFetcher {

    double fetchIncome(String range, Date startDate);
}
