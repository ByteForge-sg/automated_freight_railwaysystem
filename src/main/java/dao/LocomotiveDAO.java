package dao;

import model.Locomotive;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LocomotiveDAO {

    public void addLocomotive(Locomotive loco) throws SQLException {

        String sql = """
                INSERT INTO LOCOMOTIVE
                (Loco_ID, Model, Power_Rating_HP, Status)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, loco.getLocoId());
            ps.setString(2, loco.getModel());
            ps.setInt(3, loco.getPowerRatingHp());
            ps.setString(4, loco.getStatus());

            ps.executeUpdate();
        }
    }

    public List<Locomotive> getAllLocomotives() throws SQLException {

        List<Locomotive> locomotives = new ArrayList<>();

        String sql = "SELECT * FROM LOCOMOTIVE ORDER BY Loco_ID";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                locomotives.add(new Locomotive(
                        rs.getInt("Loco_ID"),
                        rs.getString("Model"),
                        rs.getInt("Power_Rating_HP"),
                        rs.getString("Status")
                ));
            }
        }

        return locomotives;
    }

    public void updateStatus(int locoId, String status)
            throws SQLException {

        String sql = """
                UPDATE LOCOMOTIVE
                SET Status = ?
                WHERE Loco_ID = ?
                """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, locoId);

            ps.executeUpdate();
        }
    }

    public void deleteLocomotive(int locoId) throws SQLException {

        String sql = "DELETE FROM LOCOMOTIVE WHERE Loco_ID = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, locoId);

            ps.executeUpdate();
        }
    }
}