package service;

import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TrackLockService {

    /**
     * Acquires track locks in ascending Segment_ID order.
     *
     * This ordering helps prevent circular wait conditions.
     * Locks are held until COMMIT or ROLLBACK.
     */
    public boolean reserveTracks(
            List<Integer> segmentIds,
            String transactionId) {

        if (segmentIds == null || segmentIds.isEmpty()) {
            return false;
        }

        List<Integer> orderedIds =
                new ArrayList<>(segmentIds);

        // Global lock ordering
        Collections.sort(orderedIds);

        String selectSql = """
                SELECT Segment_ID, Status
                FROM TRACK_SEGMENT
                WHERE Segment_ID = ?
                FOR UPDATE
                """;

        String updateSql = """
                UPDATE TRACK_SEGMENT
                SET Status = 'LOCKED'
                WHERE Segment_ID = ?
                """;

        String logSql = """
                INSERT INTO TRANSACTION_LOG
                (Transaction_ID, Operation, Table_Name,
                 Record_ID, Old_Value, New_Value, Status)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection con =
                     DatabaseConnection.getConnection()) {

            con.setAutoCommit(false);

            try (PreparedStatement select =
                         con.prepareStatement(selectSql);
                 PreparedStatement update =
                         con.prepareStatement(updateSql);
                 PreparedStatement log =
                         con.prepareStatement(logSql)) {

                for (Integer segmentId : orderedIds) {

                    select.setInt(1, segmentId);

                    try (ResultSet rs =
                                 select.executeQuery()) {

                        if (!rs.next()) {
                            con.rollback();
                            return false;
                        }

                        String status =
                                rs.getString("Status");

                        if (!"AVAILABLE".equalsIgnoreCase(status)) {
                            con.rollback();
                            return false;
                        }

                        update.setInt(1, segmentId);
                        update.executeUpdate();

                        log.setString(1, transactionId);
                        log.setString(2, "LOCK");
                        log.setString(3, "TRACK_SEGMENT");
                        log.setString(4, String.valueOf(segmentId));
                        log.setString(5, status);
                        log.setString(6, "LOCKED");
                        log.setString(7, "STARTED");

                        log.executeUpdate();
                    }
                }

                // Locks remain held until this COMMIT.
                con.commit();

                return true;

            } catch (SQLException e) {

                con.rollback();
                throw e;
            }

        } catch (SQLException e) {

            e.printStackTrace();
            return false;
        }
    }

    public void releaseTracks(
            List<Integer> segmentIds,
            String transactionId) {

        if (segmentIds == null || segmentIds.isEmpty()) {
            return;
        }

        String sql = """
                UPDATE TRACK_SEGMENT
                SET Status = 'AVAILABLE'
                WHERE Segment_ID = ?
                """;

        try (Connection con =
                     DatabaseConnection.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(sql)) {

            con.setAutoCommit(false);

            for (Integer segmentId : segmentIds) {

                ps.setInt(1, segmentId);
                ps.addBatch();
            }

            ps.executeBatch();

            con.commit();

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }
}