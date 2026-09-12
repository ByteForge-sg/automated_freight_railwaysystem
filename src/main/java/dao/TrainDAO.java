package dao;

import model.Train;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrainDAO {

    public void addTrain(Train train) throws SQLException {

        String sql = """
                INSERT INTO TRAIN
                (Train_ID, Train_Number, Train_Type, Current_Status)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, train.getTrainId());
            ps.setString(2, train.getTrainNumber());
            ps.setString(3, train.getTrainType());
            ps.setString(4, train.getCurrentStatus());

            ps.executeUpdate();
        }
    }

    public List<Train> getAllTrains() throws SQLException {

        List<Train> trains = new ArrayList<>();

        String sql = "SELECT * FROM TRAIN ORDER BY Train_ID";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Train train = new Train(
                        rs.getInt("Train_ID"),
                        rs.getString("Train_Number"),
                        rs.getString("Train_Type"),
                        rs.getString("Current_Status")
                );

                trains.add(train);
            }
        }

        return trains;
    }

    public void updateStatus(int trainId, String status)
            throws SQLException {

        String sql = """
                UPDATE TRAIN
                SET Current_Status = ?
                WHERE Train_ID = ?
                """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, trainId);

            ps.executeUpdate();
        }
    }

    public void deleteTrain(int trainId) throws SQLException {

        String sql = "DELETE FROM TRAIN WHERE Train_ID = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, trainId);

            ps.executeUpdate();
        }
    }
}