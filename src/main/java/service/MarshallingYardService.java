package service;

import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MarshallingYardService {

    // =========================================================
    // INNER CLASS FOR WAGON INFORMATION
    // =========================================================

    public static class WagonInfo {

        private int wagonId;
        private String wagonType;
        private double capacity;
        private String status;
        private int sequenceNo;

        public WagonInfo(
                int wagonId,
                String wagonType,
                double capacity,
                String status,
                int sequenceNo) {

            this.wagonId = wagonId;
            this.wagonType = wagonType;
            this.capacity = capacity;
            this.status = status;
            this.sequenceNo = sequenceNo;
        }

        public int getWagonId() {
            return wagonId;
        }

        public String getWagonType() {
            return wagonType;
        }

        public double getCapacity() {
            return capacity;
        }

        public String getStatus() {
            return status;
        }

        public int getSequenceNo() {
            return sequenceNo;
        }
    }

    // =========================================================
    // GET ALL TRAINS
    // =========================================================

    public List<String> getTrainList() throws SQLException {

        List<String> trains = new ArrayList<>();

        String sql =
                "SELECT Train_ID, Train_Number, Current_Status " +
                "FROM TRAIN " +
                "ORDER BY Train_ID";

        try (
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                String train =
                        rs.getInt("Train_ID")
                        + " - "
                        + rs.getString("Train_Number")
                        + " ("
                        + rs.getString("Current_Status")
                        + ")";

                trains.add(train);
            }
        }

        return trains;
    }

    // =========================================================
    // EXTRACT TRAIN ID FROM COMBOBOX TEXT
    // =========================================================

    public int extractTrainId(String trainText) {

        if (trainText == null || trainText.isBlank()) {
            return -1;
        }

        try {

            String id =
                    trainText.substring(
                            0,
                            trainText.indexOf(" - ")
                    );

            return Integer.parseInt(id.trim());

        } catch (Exception e) {

            return -1;
        }
    }

    // =========================================================
    // GET TRAIN COMPOSITION
    // =========================================================

    public List<WagonInfo> getTrainComposition(
            int trainId) throws SQLException {

        List<WagonInfo> wagons =
                new ArrayList<>();

        String sql =
                "SELECT " +
                "tc.Wagon_ID, " +
                "fc.Wagon_Type, " +
                "fc.Capacity_Tons, " +
                "fc.Current_Status, " +
                "tc.Sequence_No " +
                "FROM TRAIN_COMPOSITION tc " +
                "JOIN FREIGHT_CAR fc " +
                "ON tc.Wagon_ID = fc.Wagon_ID " +
                "WHERE tc.Train_ID = ? " +
                "ORDER BY tc.Sequence_No";

        try (
                Connection con =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setInt(1, trainId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    wagons.add(
                            new WagonInfo(
                                    rs.getInt("Wagon_ID"),
                                    rs.getString("Wagon_Type"),
                                    rs.getDouble("Capacity_Tons"),
                                    rs.getString("Current_Status"),
                                    rs.getInt("Sequence_No")
                            )
                    );
                }
            }
        }

        return wagons;
    }

    // =========================================================
    // MOVE WAGONS BETWEEN TRAINS
    //
    // Entire operation is performed as ONE transaction.
    // If any step fails, everything is rolled back.
    // =========================================================

    public void moveWagons(
            int sourceTrainId,
            int destinationTrainId,
            List<Integer> wagonIds) throws SQLException {

        if (sourceTrainId == destinationTrainId) {

            throw new SQLException(
                    "Source and destination trains cannot be the same."
            );
        }

        if (wagonIds == null || wagonIds.isEmpty()) {

            throw new SQLException(
                    "Please select at least one wagon."
            );
        }

        Connection con = null;

        try {

            con =
                    DatabaseConnection.getConnection();

            // Start transaction
            con.setAutoCommit(false);

            // -------------------------------------------------
            // STEP 1: Lock source and destination train rows
            // -------------------------------------------------

            String lockTrainSql =
                    "SELECT Train_ID " +
                    "FROM TRAIN " +
                    "WHERE Train_ID IN (?, ?) " +
                    "ORDER BY Train_ID " +
                    "FOR UPDATE";

            try (
                    PreparedStatement ps =
                            con.prepareStatement(lockTrainSql)
            ) {

                ps.setInt(1, sourceTrainId);
                ps.setInt(2, destinationTrainId);

                try (ResultSet rs = ps.executeQuery()) {

                    int count = 0;

                    while (rs.next()) {
                        count++;
                    }

                    if (count != 2) {

                        throw new SQLException(
                                "Invalid source or destination train."
                        );
                    }
                }
            }

            // -------------------------------------------------
            // STEP 2: Determine current maximum sequence
            // -------------------------------------------------

            int nextSequence = 1;

            String maxSequenceSql =
                    "SELECT COALESCE(MAX(Sequence_No), 0) + 1 " +
                    "FROM TRAIN_COMPOSITION " +
                    "WHERE Train_ID = ?";

            try (
                    PreparedStatement ps =
                            con.prepareStatement(maxSequenceSql)
            ) {

                ps.setInt(1, destinationTrainId);

                try (ResultSet rs = ps.executeQuery()) {

                    if (rs.next()) {
                        nextSequence =
                                rs.getInt(1);
                    }
                }
            }

            // -------------------------------------------------
            // STEP 3: Lock and validate every selected wagon
            // -------------------------------------------------

            String wagonCheckSql =
                    "SELECT Wagon_ID " +
                    "FROM TRAIN_COMPOSITION " +
                    "WHERE Train_ID = ? " +
                    "AND Wagon_ID = ? " +
                    "FOR UPDATE";

            // -------------------------------------------------
            // STEP 4: Insert wagon into destination train
            // -------------------------------------------------

            String insertSql =
                    "INSERT INTO TRAIN_COMPOSITION " +
                    "(Train_ID, Wagon_ID, Sequence_No) " +
                    "VALUES (?, ?, ?)";

            // -------------------------------------------------
            // STEP 5: Remove wagon from source train
            // -------------------------------------------------

            String deleteSql =
                    "DELETE FROM TRAIN_COMPOSITION " +
                    "WHERE Train_ID = ? " +
                    "AND Wagon_ID = ?";

            try (
                    PreparedStatement checkPs =
                            con.prepareStatement(wagonCheckSql);

                    PreparedStatement insertPs =
                            con.prepareStatement(insertSql);

                    PreparedStatement deletePs =
                            con.prepareStatement(deleteSql)
            ) {

                for (int wagonId : wagonIds) {

                    // Validate wagon belongs to source train
                    checkPs.setInt(1, sourceTrainId);
                    checkPs.setInt(2, wagonId);

                    boolean exists;

                    try (
                            ResultSet rs =
                                    checkPs.executeQuery()
                    ) {

                        exists = rs.next();
                    }

                    if (!exists) {

                        throw new SQLException(
                                "Wagon "
                                + wagonId
                                + " does not belong to source train."
                        );
                    }

                    // Insert into destination
                    insertPs.setInt(
                            1,
                            destinationTrainId
                    );

                    insertPs.setInt(
                            2,
                            wagonId
                    );

                    insertPs.setInt(
                            3,
                            nextSequence++
                    );

                    insertPs.executeUpdate();

                    // Delete from source
                    deletePs.setInt(
                            1,
                            sourceTrainId
                    );

                    deletePs.setInt(
                            2,
                            wagonId
                    );

                    deletePs.executeUpdate();
                }
            }

            // -------------------------------------------------
            // STEP 6: Commit complete operation
            // -------------------------------------------------

            con.commit();

        } catch (SQLException e) {

            // -------------------------------------------------
            // ROLLBACK ON FAILURE
            // -------------------------------------------------

            if (con != null) {

                try {
                    con.rollback();
                } catch (SQLException rollbackError) {
                    rollbackError.printStackTrace();
                }
            }

            throw e;

        } finally {

            if (con != null) {

                try {
                    con.setAutoCommit(true);
                    con.close();

                } catch (SQLException closeError) {
                    closeError.printStackTrace();
                }
            }
        }
    }
        // =========================================================
    // DISPLAY TRAIN COMPOSITION
    // Compatibility method used by TestServices
    // =========================================================

    public void displayTrainComposition(
            int trainId) throws SQLException {

        List<WagonInfo> wagons =
                getTrainComposition(trainId);

        System.out.println(
                "\nTrain Composition for Train ID: "
                + trainId
        );

        if (wagons.isEmpty()) {

            System.out.println(
                    "No wagons assigned to this train."
            );

            return;
        }

        System.out.println(
                "------------------------------------------------------------"
        );

        System.out.printf(
                "%-10s %-12s %-15s %-15s %-10s%n",
                "Sequence",
                "Wagon ID",
                "Wagon Type",
                "Capacity",
                "Status"
        );

        System.out.println(
                "------------------------------------------------------------"
        );

        for (WagonInfo wagon : wagons) {

            System.out.printf(
                    "%-10d %-12d %-15s %-15.2f %-10s%n",
                    wagon.getSequenceNo(),
                    wagon.getWagonId(),
                    wagon.getWagonType(),
                    wagon.getCapacity(),
                    wagon.getStatus()
            );
        }

        System.out.println(
                "------------------------------------------------------------"
        );
    }
}