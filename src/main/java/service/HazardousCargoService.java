package service;

import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HazardousCargoService {

    public List<String> getHazardousCargo()
            throws SQLException {

        List<String> results = new ArrayList<>();

        String sql = """
                SELECT
                    c.Cargo_ID,
                    c.Description,
                    c.Weight_Tons,
                    c.Hazard_Class,
                    cc.Wagon_ID
                FROM CARGO c
                JOIN CAR_CARGO cc
                    ON c.Cargo_ID = cc.Cargo_ID
                WHERE c.Hazard_Class LIKE 'HAZARDOUS%'
                ORDER BY c.Cargo_ID
                """;

        try (Connection con =
                     DatabaseConnection.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                String result =
                        "Cargo " +
                        rs.getInt("Cargo_ID") +
                        " | " +
                        rs.getString("Description") +
                        " | " +
                        rs.getDouble("Weight_Tons") +
                        " tons | Hazard: " +
                        rs.getString("Hazard_Class") +
                        " | Wagon: " +
                        rs.getInt("Wagon_ID");

                results.add(result);
            }
        }

        return results;
    }
}