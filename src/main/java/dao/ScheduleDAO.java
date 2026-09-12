package dao;

import util.DatabaseConnection;

import java.sql.*;

public class ScheduleDAO {

    public void updateLockStatus(int scheduleId, String lockStatus)
            throws SQLException {

        String sql = """
                UPDATE SCHEDULE
                SET Lock_Status = ?
                WHERE Schedule_ID = ?
                """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, lockStatus);
            ps.setInt(2, scheduleId);

            ps.executeUpdate();
        }
    }

    public void displaySchedules() throws SQLException {

        String sql = """
                SELECT
                    s.Schedule_ID,
                    t.Train_Number,
                    s.Segment_ID,
                    s.Planned_Start,
                    s.Planned_End,
                    s.Lock_Status
                FROM SCHEDULE s
                JOIN TRAIN t
                    ON s.Train_ID = t.Train_ID
                ORDER BY s.Schedule_ID
                """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                System.out.println(
                        rs.getInt("Schedule_ID")
                        + " | Train "
                        + rs.getString("Train_Number")
                        + " | Segment "
                        + rs.getInt("Segment_ID")
                        + " | "
                        + rs.getTimestamp("Planned_Start")
                        + " -> "
                        + rs.getTimestamp("Planned_End")
                        + " | Lock: "
                        + rs.getString("Lock_Status")
                );
            }
        }
    }
}