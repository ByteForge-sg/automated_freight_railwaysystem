package dao;

import util.DatabaseConnection;

import java.sql.*;

public class ManifestDAO {

    public void addManifest(
            int manifestId,
            int trainId,
            int originStationId,
            int destinationStationId,
            Timestamp departureTime,
            Timestamp arrivalTime) throws SQLException {

        String sql = """
                INSERT INTO MANIFEST
                (Manifest_ID, Train_ID, Origin_Station_ID,
                 Dest_Station_ID, Departure_Time, Arrival_Time)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, manifestId);
            ps.setInt(2, trainId);
            ps.setInt(3, originStationId);
            ps.setInt(4, destinationStationId);
            ps.setTimestamp(5, departureTime);
            ps.setTimestamp(6, arrivalTime);

            ps.executeUpdate();
        }
    }

    public void displayTrainManifests() throws SQLException {

        String sql = """
                SELECT
                    t.Train_Number,
                    s1.Station_Name AS Origin,
                    s2.Station_Name AS Destination,
                    m.Departure_Time,
                    m.Arrival_Time
                FROM MANIFEST m
                JOIN TRAIN t
                    ON m.Train_ID = t.Train_ID
                JOIN STATION s1
                    ON m.Origin_Station_ID = s1.Station_ID
                JOIN STATION s2
                    ON m.Dest_Station_ID = s2.Station_ID
                ORDER BY m.Departure_Time
                """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                System.out.println(
                        rs.getString("Train_Number")
                        + " | "
                        + rs.getString("Origin")
                        + " -> "
                        + rs.getString("Destination")
                        + " | Departure: "
                        + rs.getTimestamp("Departure_Time")
                        + " | Arrival: "
                        + rs.getTimestamp("Arrival_Time")
                );
            }
        }
    }
}