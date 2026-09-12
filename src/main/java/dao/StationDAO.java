package dao;

import model.Station;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StationDAO {

    public void addStation(Station station) throws SQLException {

        String sql = """
                INSERT INTO STATION
                (Station_ID, Station_Name, Location, Station_Type)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, station.getStationId());
            ps.setString(2, station.getStationName());
            ps.setString(3, station.getLocation());
            ps.setString(4, station.getStationType());

            ps.executeUpdate();
        }
    }

    public List<Station> getAllStations() throws SQLException {

        List<Station> stations = new ArrayList<>();

        String sql = "SELECT * FROM STATION ORDER BY Station_ID";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Station station = new Station(
                        rs.getInt("Station_ID"),
                        rs.getString("Station_Name"),
                        rs.getString("Location"),
                        rs.getString("Station_Type")
                );

                stations.add(station);
            }
        }

        return stations;
    }

    public void updateStation(Station station) throws SQLException {

        String sql = """
                UPDATE STATION
                SET Station_Name = ?,
                    Location = ?,
                    Station_Type = ?
                WHERE Station_ID = ?
                """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, station.getStationName());
            ps.setString(2, station.getLocation());
            ps.setString(3, station.getStationType());
            ps.setInt(4, station.getStationId());

            ps.executeUpdate();
        }
    }

    public void deleteStation(int stationId) throws SQLException {

        String sql = "DELETE FROM STATION WHERE Station_ID = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, stationId);

            ps.executeUpdate();
        }
    }
}
