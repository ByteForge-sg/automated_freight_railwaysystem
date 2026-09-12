package dao;

import util.DatabaseConnection;

import java.sql.*;

public class MarshallingYardDAO {

    public void displayYards() throws SQLException {

        String sql = """
                SELECT
                    y.Yard_ID,
                    s.Station_Name,
                    y.Num_Tracks,
                    y.Capacity
                FROM MARSHALLING_YARD y
                JOIN STATION s
                    ON y.Station_ID = s.Station_ID
                ORDER BY y.Yard_ID
                """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                System.out.println(
                        "Yard "
                        + rs.getInt("Yard_ID")
                        + " | Station: "
                        + rs.getString("Station_Name")
                        + " | Tracks: "
                        + rs.getInt("Num_Tracks")
                        + " | Capacity: "
                        + rs.getInt("Capacity")
                );
            }
        }
    }
}