package dao;

import model.FreightCar;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FreightCarDAO {

    public void addFreightCar(FreightCar car) throws SQLException {

        String sql = """
                INSERT INTO FREIGHT_CAR
                (Wagon_ID, Wagon_Type, Capacity_Tons, Current_Status)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, car.getWagonId());
            ps.setString(2, car.getWagonType());
            ps.setDouble(3, car.getCapacityTons());
            ps.setString(4, car.getCurrentStatus());

            ps.executeUpdate();
        }
    }

    public List<FreightCar> getAllFreightCars() throws SQLException {

        List<FreightCar> cars = new ArrayList<>();

        String sql = "SELECT * FROM FREIGHT_CAR ORDER BY Wagon_ID";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                cars.add(new FreightCar(
                        rs.getInt("Wagon_ID"),
                        rs.getString("Wagon_Type"),
                        rs.getDouble("Capacity_Tons"),
                        rs.getString("Current_Status")
                ));
            }
        }

        return cars;
    }

    public void updateStatus(int wagonId, String status)
            throws SQLException {

        String sql = """
                UPDATE FREIGHT_CAR
                SET Current_Status = ?
                WHERE Wagon_ID = ?
                """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, wagonId);

            ps.executeUpdate();
        }
    }

    public void deleteFreightCar(int wagonId) throws SQLException {

        String sql = "DELETE FROM FREIGHT_CAR WHERE Wagon_ID = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, wagonId);

            ps.executeUpdate();
        }
    }
}