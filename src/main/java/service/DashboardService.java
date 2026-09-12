package service;

import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DashboardService {

    public int getStationCount() throws SQLException {
        return getCount("SELECT COUNT(*) FROM STATION");
    }

    public int getTrainCount() throws SQLException {
        return getCount("SELECT COUNT(*) FROM TRAIN");
    }

    public int getTrackCount() throws SQLException {
        return getCount("SELECT COUNT(*) FROM TRACK_SEGMENT");
    }

    public int getFreightCarCount() throws SQLException {
        return getCount("SELECT COUNT(*) FROM FREIGHT_CAR");
    }

    public int getCargoCount() throws SQLException {
        return getCount("SELECT COUNT(*) FROM CARGO");
    }

    public int getHazardousCargoCount() throws SQLException {

        String sql = """
                SELECT COUNT(*)
                FROM CARGO
                WHERE Hazard_Class LIKE 'HAZARDOUS%'
                """;

        return getCount(sql);
    }

    private int getCount(String sql) throws SQLException {

        try (Connection con =
                     DatabaseConnection.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(sql);
             ResultSet rs =
                     ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        }

        return 0;
    }
}