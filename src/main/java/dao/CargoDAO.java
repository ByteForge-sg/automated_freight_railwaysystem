package dao;

import model.Cargo;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CargoDAO {

    public void addCargo(Cargo cargo) throws SQLException {

        String sql = """
                INSERT INTO CARGO
                (Cargo_ID, Description, Weight_Tons, Hazard_Class)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, cargo.getCargoId());
            ps.setString(2, cargo.getDescription());
            ps.setDouble(3, cargo.getWeightTons());
            ps.setString(4, cargo.getHazardClass());

            ps.executeUpdate();
        }
    }

    public List<Cargo> getAllCargo() throws SQLException {

        List<Cargo> cargoList = new ArrayList<>();

        String sql = "SELECT * FROM CARGO ORDER BY Cargo_ID";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                cargoList.add(new Cargo(
                        rs.getInt("Cargo_ID"),
                        rs.getString("Description"),
                        rs.getDouble("Weight_Tons"),
                        rs.getString("Hazard_Class")
                ));
            }
        }

        return cargoList;
    }

    public void updateCargo(Cargo cargo) throws SQLException {

        String sql = """
                UPDATE CARGO
                SET Description = ?,
                    Weight_Tons = ?,
                    Hazard_Class = ?
                WHERE Cargo_ID = ?
                """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, cargo.getDescription());
            ps.setDouble(2, cargo.getWeightTons());
            ps.setString(3, cargo.getHazardClass());
            ps.setInt(4, cargo.getCargoId());

            ps.executeUpdate();
        }
    }

    public void deleteCargo(int cargoId) throws SQLException {

        String sql = "DELETE FROM CARGO WHERE Cargo_ID = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, cargoId);

            ps.executeUpdate();
        }
    }
}