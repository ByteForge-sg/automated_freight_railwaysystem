package service;

import util.DatabaseConnection;

import java.sql.*;

public class MaintenanceService {

    public boolean isUnderMaintenance(
            String assetType,
            int assetId) throws SQLException {

        String sql = """
                SELECT COUNT(*)
                FROM MAINTENANCE_RECORD
                WHERE Asset_Type = ?
                  AND Asset_ID = ?
                  AND Status IN ('SCHEDULED', 'IN_PROGRESS')
                """;

        try (Connection con =
                     DatabaseConnection.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(sql)) {

            ps.setString(1, assetType);
            ps.setInt(2, assetId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }

        return false;
    }

    public boolean canOperate(
            String assetType,
            int assetId) throws SQLException {

        return !isUnderMaintenance(
                assetType,
                assetId
        );
    }
}