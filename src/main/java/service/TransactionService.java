package service;

import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TransactionService {

    private Connection transactionConnection;

    private String transactionId;

    private final List<Integer> lockedSegments =
            new ArrayList<>();

    // =========================================================
    // TRACK INFORMATION
    // =========================================================

    public static class TrackInfo {

        private int segmentId;
        private int fromStationId;
        private int toStationId;
        private double lengthKm;
        private double maxSpeed;
        private String status;

        public TrackInfo(
                int segmentId,
                int fromStationId,
                int toStationId,
                double lengthKm,
                double maxSpeed,
                String status) {

            this.segmentId = segmentId;
            this.fromStationId = fromStationId;
            this.toStationId = toStationId;
            this.lengthKm = lengthKm;
            this.maxSpeed = maxSpeed;
            this.status = status;
        }

        public int getSegmentId() {
            return segmentId;
        }

        public int getFromStationId() {
            return fromStationId;
        }

        public int getToStationId() {
            return toStationId;
        }

        public double getLengthKm() {
            return lengthKm;
        }

        public double getMaxSpeed() {
            return maxSpeed;
        }

        public String getStatus() {
            return status;
        }
    }

    // =========================================================
    // TRANSACTION LOG INFORMATION
    // =========================================================

    public static class LogInfo {

        private int logId;
        private String transactionId;
        private String operation;
        private String tableName;
        private String recordId;
        private String oldValue;
        private String newValue;
        private Timestamp logTime;
        private String status;

        public LogInfo(
                int logId,
                String transactionId,
                String operation,
                String tableName,
                String recordId,
                String oldValue,
                String newValue,
                Timestamp logTime,
                String status) {

            this.logId = logId;
            this.transactionId = transactionId;
            this.operation = operation;
            this.tableName = tableName;
            this.recordId = recordId;
            this.oldValue = oldValue;
            this.newValue = newValue;
            this.logTime = logTime;
            this.status = status;
        }

        public int getLogId() {
            return logId;
        }

        public String getTransactionId() {
            return transactionId;
        }

        public String getOperation() {
            return operation;
        }

        public String getTableName() {
            return tableName;
        }

        public String getRecordId() {
            return recordId;
        }

        public String getOldValue() {
            return oldValue;
        }

        public String getNewValue() {
            return newValue;
        }

        public Timestamp getLogTime() {
            return logTime;
        }

        public String getStatus() {
            return status;
        }
    }

    // =========================================================
    // GET TRACKS
    // =========================================================

    public List<TrackInfo> getAllTracks()
            throws SQLException {

        List<TrackInfo> tracks =
                new ArrayList<>();

        String sql =
                "SELECT Segment_ID, " +
                "From_Station_ID, " +
                "To_Station_ID, " +
                "Length_KM, " +
                "Max_Speed, " +
                "Status " +
                "FROM TRACK_SEGMENT " +
                "ORDER BY Segment_ID";

        try (
                Connection con =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql);

                ResultSet rs =
                        ps.executeQuery()
        ) {

            while (rs.next()) {

                tracks.add(
                        new TrackInfo(
                                rs.getInt("Segment_ID"),
                                rs.getInt("From_Station_ID"),
                                rs.getInt("To_Station_ID"),
                                rs.getDouble("Length_KM"),
                                rs.getDouble("Max_Speed"),
                                rs.getString("Status")
                        )
                );
            }
        }

        return tracks;
    }

    // =========================================================
    // CHECK WHETHER A TRANSACTION IS ACTIVE
    // =========================================================

    public boolean isTransactionActive() {

        try {

            return transactionConnection != null
                    && !transactionConnection.isClosed();

        } catch (SQLException e) {

            return false;
        }
    }

    // =========================================================
    // GET CURRENT TRANSACTION ID
    // =========================================================

    public String getTransactionId() {

        return transactionId;
    }

    // =========================================================
    // GET LOCKED TRACK COUNT
    // =========================================================

    public int getLockedTrackCount() {

        return lockedSegments.size();
    }

    // =========================================================
    // BEGIN TRANSACTION
    //
    // 1. Opens one database connection
    // 2. Disables auto-commit
    // 3. Locks selected rows using FOR UPDATE
    // 4. Changes their state to LOCKED
    //
    // Locks remain until COMMIT or ROLLBACK.
    // =========================================================

    public void beginTransaction(
            List<Integer> segmentIds)
            throws SQLException {

        if (isTransactionActive()) {

            throw new SQLException(
                    "A transaction is already active."
            );
        }

        if (segmentIds == null ||
                segmentIds.isEmpty()) {

            throw new SQLException(
                    "Please select at least one track."
            );
        }

        // Generate transaction ID
        transactionId =
                "TXN-"
                + UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase();

        transactionConnection =
                DatabaseConnection.getConnection();

        try {

            transactionConnection.setAutoCommit(false);

            /*
             * IMPORTANT FOR DEADLOCK PREVENTION:
             * Always acquire locks in ascending Segment_ID order.
             */
            List<Integer> sortedIds =
                    new ArrayList<>(segmentIds);

            sortedIds.sort(Integer::compareTo);

            String selectSql =
                    "SELECT Segment_ID, Status " +
                    "FROM TRACK_SEGMENT " +
                    "WHERE Segment_ID = ? " +
                    "FOR UPDATE";

            String updateSql =
                    "UPDATE TRACK_SEGMENT " +
                    "SET Status = 'LOCKED' " +
                    "WHERE Segment_ID = ?";

            try (
                    PreparedStatement selectPs =
                            transactionConnection
                                    .prepareStatement(selectSql);

                    PreparedStatement updatePs =
                            transactionConnection
                                    .prepareStatement(updateSql)
            ) {

                for (int segmentId : sortedIds) {

                    // -------------------------------------------------
                    // Acquire row lock
                    // -------------------------------------------------

                    selectPs.setInt(
                            1,
                            segmentId
                    );

                    String currentStatus;

                    try (
                            ResultSet rs =
                                    selectPs.executeQuery()
                    ) {

                        if (!rs.next()) {

                            throw new SQLException(
                                    "Track segment "
                                    + segmentId
                                    + " does not exist."
                            );
                        }

                        currentStatus =
                                rs.getString("Status");
                    }

                    // -------------------------------------------------
                    // Track must be available
                    // -------------------------------------------------

                    if (!"AVAILABLE".equalsIgnoreCase(
                            currentStatus)) {

                        throw new SQLException(
                                "Track segment "
                                + segmentId
                                + " is currently "
                                + currentStatus
                                + "."
                        );
                    }

                    // -------------------------------------------------
                    // Lock track
                    // -------------------------------------------------

                    updatePs.setInt(
                            1,
                            segmentId
                    );

                    updatePs.executeUpdate();

                    lockedSegments.add(
                            segmentId
                    );
                }
            }

            /*
             * Log successful transaction start
             * using a separate connection so the audit
             * record survives a later rollback.
             */
            insertAuditLog(
                    transactionId,
                    "LOCK",
                    "TRACK_SEGMENT",
                    "MULTIPLE",
                    "AVAILABLE",
                    "LOCKED",
                    "STARTED"
            );

        } catch (SQLException e) {

            /*
             * Something failed while acquiring locks.
             * Roll back everything already locked.
             */
            try {

                transactionConnection.rollback();

            } catch (SQLException rollbackError) {

                rollbackError.printStackTrace();
            }

            insertAuditLog(
                    transactionId,
                    "LOCK",
                    "TRACK_SEGMENT",
                    "MULTIPLE",
                    "AVAILABLE",
                    "LOCKED",
                    "ROLLED_BACK"
            );

            closeTransactionConnection();

            lockedSegments.clear();

            throw e;
        }
    }

    // =========================================================
    // COMMIT TRANSACTION
    //
    // LOCKED -> OCCUPIED
    // COMMIT releases all InnoDB locks.
    // =========================================================

    public void commitTransaction()
            throws SQLException {

        if (!isTransactionActive()) {

            throw new SQLException(
                    "No active transaction."
            );
        }

        try {

            String sql =
                    "UPDATE TRACK_SEGMENT " +
                    "SET Status = 'OCCUPIED' " +
                    "WHERE Segment_ID = ?";

            try (
                    PreparedStatement ps =
                            transactionConnection
                                    .prepareStatement(sql)
            ) {

                for (int segmentId :
                        lockedSegments) {

                    ps.setInt(
                            1,
                            segmentId
                    );

                    ps.executeUpdate();
                }
            }

            transactionConnection.commit();

            insertAuditLog(
                    transactionId,
                    "COMMIT",
                    "TRACK_SEGMENT",
                    "MULTIPLE",
                    "LOCKED",
                    "OCCUPIED",
                    "COMMITTED"
            );

        } catch (SQLException e) {

            try {

                transactionConnection.rollback();

            } catch (SQLException rollbackError) {

                rollbackError.printStackTrace();
            }

            insertAuditLog(
                    transactionId,
                    "COMMIT",
                    "TRACK_SEGMENT",
                    "MULTIPLE",
                    "LOCKED",
                    "OCCUPIED",
                    "ROLLED_BACK"
            );

            throw e;

        } finally {

            closeTransactionConnection();

            lockedSegments.clear();

            transactionId = null;
        }
    }

    // =========================================================
    // ROLLBACK TRANSACTION
    //
    // LOCKED -> AVAILABLE
    // =========================================================

    public void rollbackTransaction()
            throws SQLException {

        if (!isTransactionActive()) {

            throw new SQLException(
                    "No active transaction."
            );
        }

        String currentTransactionId =
                transactionId;

        try {

            transactionConnection.rollback();

            /*
             * The rollback automatically restores
             * the original AVAILABLE state.
             */

            insertAuditLog(
                    currentTransactionId,
                    "ROLLBACK",
                    "TRACK_SEGMENT",
                    "MULTIPLE",
                    "LOCKED",
                    "AVAILABLE",
                    "ROLLED_BACK"
            );

        } finally {

            closeTransactionConnection();

            lockedSegments.clear();

            transactionId = null;
        }
    }

    // =========================================================
    // FORCE CLEANUP
    // =========================================================

    public void closeTransactionConnection() {

        if (transactionConnection != null) {

            try {

                transactionConnection.close();

            } catch (SQLException e) {

                e.printStackTrace();
            }

            transactionConnection = null;
        }
    }

    // =========================================================
    // INSERT AUDIT LOG
    // Uses a separate connection so logs survive rollback.
    // =========================================================

    private void insertAuditLog(
            String txnId,
            String operation,
            String tableName,
            String recordId,
            String oldValue,
            String newValue,
            String status)
            throws SQLException {

        String sql =
                "INSERT INTO TRANSACTION_LOG " +
                "(Transaction_ID, Operation, Table_Name, " +
                "Record_ID, Old_Value, New_Value, Status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection con =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setString(
                    1,
                    txnId
            );

            ps.setString(
                    2,
                    operation
            );

            ps.setString(
                    3,
                    tableName
            );

            ps.setString(
                    4,
                    recordId
            );

            ps.setString(
                    5,
                    oldValue
            );

            ps.setString(
                    6,
                    newValue
            );

            ps.setString(
                    7,
                    status
            );

            ps.executeUpdate();
        }
    }

    // =========================================================
    // GET TRANSACTION LOG
    // =========================================================

    public List<LogInfo> getTransactionLogs()
            throws SQLException {

        List<LogInfo> logs =
                new ArrayList<>();

        String sql =
                "SELECT Log_ID, Transaction_ID, " +
                "Operation, Table_Name, Record_ID, " +
                "Old_Value, New_Value, Log_Time, Status " +
                "FROM TRANSACTION_LOG " +
                "ORDER BY Log_ID DESC";

        try (
                Connection con =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql);

                ResultSet rs =
                        ps.executeQuery()
        ) {

            while (rs.next()) {

                logs.add(
                        new LogInfo(
                                rs.getInt("Log_ID"),
                                rs.getString("Transaction_ID"),
                                rs.getString("Operation"),
                                rs.getString("Table_Name"),
                                rs.getString("Record_ID"),
                                rs.getString("Old_Value"),
                                rs.getString("New_Value"),
                                rs.getTimestamp("Log_Time"),
                                rs.getString("Status")
                        )
                );
            }
        }

        return logs;
    }
}