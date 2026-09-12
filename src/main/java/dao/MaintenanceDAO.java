package dao;

import util.DatabaseConnection;

import java.sql.*;

public class MaintenanceDAO {

    public void displayMaintenanceRecords() throws SQLException {

        String sql = """
                SELECT
                    Maint_ID,
                    Asset_Type,
                    Asset_ID,
                    Maint_Date,
                    Cost,
                    Status
                FROM MAINTENANCE_RECORD
                ORDER BY Maint_Date DESC
                """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                System.out.println(
                        rs.getInt("Maint_ID")
                        + " | "
                        + rs.getString("Asset_Type")
                        + " "
                        + rs.getInt("Asset_ID")
                        + " | Date: "
                        + rs.getDate("Maint_Date")
                        + " | Cost: "
                        + rs.getDouble("Cost")
                        + " | "
                        + rs.getString("Status")
                );
            }
        }
    }
}