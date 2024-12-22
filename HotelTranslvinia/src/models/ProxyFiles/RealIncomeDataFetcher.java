package models.ProxyFiles;
import main.Database;

import java.sql.*;

public class RealIncomeDataFetcher implements IncomeDataFetcher{
    @Override
    public double fetchIncome(String range, Date startDate) {
        double totalIncome = 0.0;

        // SQL Query
        String query = "";
        switch (range.toLowerCase()) {
            case "weekly":
                query = "SELECT SUM(total_cost) AS income FROM resident WHERE checkInDate BETWEEN ? AND DATE_ADD(?, INTERVAL 7 DAY)";
                break;
            case "monthly":
                query = "SELECT SUM(total_cost) AS income FROM resident WHERE MONTH(checkInDate) = MONTH(?) AND YEAR(checkInDate) = YEAR(?)";
                break;
            case "annual":
                query = "SELECT SUM(total_cost) AS income FROM resident WHERE YEAR(checkInDate) = YEAR(?)";
                break;
            default:
                throw new IllegalArgumentException("Invalid range: " + range);
        }

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setDate(1, startDate);
            if (range.equalsIgnoreCase("weekly")) {
                statement.setDate(2, startDate);
            } else if (range.equalsIgnoreCase("monthly") || range.equalsIgnoreCase("annual")) {
                statement.setDate(2, startDate); // Second date for month/year filtering
            }

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                totalIncome = rs.getDouble("income");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalIncome;
    }
}
