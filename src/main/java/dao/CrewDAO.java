package dao;

import util.DatabaseConnection;

import java.sql.*;

public class CrewDAO {

    public void displayCrewAssignments() throws SQLException {

        String sql = """
                SELECT
                    c.Name,
                    c.Role,
                    t.Train_Number,
                    ca.Role_On_Duty
                FROM CREW_ASSIGNMENT ca
                JOIN CREW c
                    ON ca.Crew_ID = c.Crew_ID
                JOIN TRAIN t
                    ON ca.Train_ID = t.Train_ID
                ORDER BY t.Train_Number
                """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                System.out.println(
                        rs.getString("Name")
                        + " | "
                        + rs.getString("Role")
                        + " | Train "
                        + rs.getString("Train_Number")
                        + " | Duty: "
                        + rs.getString("Role_On_Duty")
                );
            }
        }
    }
}