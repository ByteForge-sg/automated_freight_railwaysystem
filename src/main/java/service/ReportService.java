package service;

import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReportService {

    // =========================================================
    // GENERIC REPORT DATA
    // =========================================================

    public static class ReportData {

        private String[] columns;
        private List<Object[]> rows;

        public ReportData(
                String[] columns,
                List<Object[]> rows) {

            this.columns = columns;
            this.rows = rows;
        }

        public String[] getColumns() {
            return columns;
        }

        public List<Object[]> getRows() {
            return rows;
        }
    }

    // =========================================================
    // TRAIN OVERVIEW REPORT
    // Uses train_overview view
    // =========================================================

    public ReportData getTrainOverview()
            throws SQLException {

        String sql =
                "SELECT Train_ID, Train_Number, Train_Type, " +
                "Current_Status, Origin_Station, " +
                "Destination_Station, Departure_Time, Arrival_Time " +
                "FROM train_overview " +
                "ORDER BY Train_ID";

        return executeQuery(
                sql,
                new String[]{
                        "Train ID",
                        "Train Number",
                        "Type",
                        "Status",
                        "Origin",
                        "Destination",
                        "Departure",
                        "Arrival"
                }
        );
    }

    // =========================================================
    // HAZARDOUS CARGO REPORT
    // Uses hazardous_cargo view
    // =========================================================

    public ReportData getHazardousCargo()
            throws SQLException {

        String sql =
                "SELECT Cargo_ID, Description, Weight_Tons, " +
                "Hazard_Class, Wagon_ID " +
                "FROM hazardous_cargo " +
                "ORDER BY Hazard_Class, Cargo_ID";

        return executeQuery(
                sql,
                new String[]{
                        "Cargo ID",
                        "Description",
                        "Weight (Tons)",
                        "Hazard Class",
                        "Wagon ID"
                }
        );
    }

    // =========================================================
    // TRAIN COMPOSITION REPORT
    // Uses train_composition_view
    // =========================================================

    public ReportData getTrainComposition()
            throws SQLException {

        String sql =
                "SELECT Train_Number, Sequence_No, Wagon_ID, " +
                "Wagon_Type, Capacity_Tons " +
                "FROM train_composition_view " +
                "ORDER BY Train_Number, Sequence_No";

        return executeQuery(
                sql,
                new String[]{
                        "Train Number",
                        "Sequence",
                        "Wagon ID",
                        "Wagon Type",
                        "Capacity (Tons)"
                }
        );
    }

    // =========================================================
    // MAINTENANCE REPORT
    // =========================================================

    public ReportData getMaintenanceReport()
            throws SQLException {

        String sql =
                "SELECT Maint_ID, Asset_Type, Asset_ID, " +
                "Maint_Date, Cost, Status " +
                "FROM MAINTENANCE_RECORD " +
                "ORDER BY Maint_Date DESC";

        return executeQuery(
                sql,
                new String[]{
                        "Maintenance ID",
                        "Asset Type",
                        "Asset ID",
                        "Date",
                        "Cost",
                        "Status"
                }
        );
    }

    // =========================================================
    // TRACK UTILIZATION REPORT
    // Uses GROUP BY and COUNT
    // =========================================================

    public ReportData getTrackUtilization()
            throws SQLException {

        String sql =
                "SELECT Status, COUNT(*) AS Track_Count, " +
                "ROUND(SUM(Length_KM), 2) AS Total_Length_KM " +
                "FROM TRACK_SEGMENT " +
                "GROUP BY Status " +
                "ORDER BY Status";

        return executeQuery(
                sql,
                new String[]{
                        "Track Status",
                        "Number of Tracks",
                        "Total Length (KM)"
                }
        );
    }

    // =========================================================
    // CARGO SUMMARY
    // Uses GROUP BY and aggregate functions
    // =========================================================

    public ReportData getCargoSummary()
            throws SQLException {

        String sql =
                "SELECT " +
                "CASE " +
                "WHEN Hazard_Class LIKE 'HAZARDOUS%' " +
                "THEN 'Hazardous' " +
                "ELSE 'Non-Hazardous' " +
                "END AS Cargo_Category, " +
                "COUNT(*) AS Cargo_Count, " +
                "ROUND(SUM(Weight_Tons), 2) AS Total_Weight_Tons " +
                "FROM CARGO " +
                "GROUP BY Cargo_Category " +
                "ORDER BY Cargo_Category";

        return executeQuery(
                sql,
                new String[]{
                        "Cargo Category",
                        "Cargo Count",
                        "Total Weight (Tons)"
                }
        );
    }

    // =========================================================
    // TRAIN STATUS SUMMARY
    // Uses GROUP BY and COUNT
    // =========================================================

    public ReportData getTrainStatusSummary()
            throws SQLException {

        String sql =
                "SELECT Current_Status, " +
                "COUNT(*) AS Train_Count " +
                "FROM TRAIN " +
                "GROUP BY Current_Status " +
                "ORDER BY Current_Status";

        return executeQuery(
                sql,
                new String[]{
                        "Train Status",
                        "Number of Trains"
                }
        );
    }

    // =========================================================
    // GENERAL QUERY EXECUTOR
    // =========================================================

    private ReportData executeQuery(
            String sql,
            String[] columns)
            throws SQLException {

        List<Object[]> rows =
                new ArrayList<>();

        try (
                Connection con =
                        DatabaseConnection.getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql);

                ResultSet rs =
                        ps.executeQuery()
        ) {

            while (rs.next()) {

                Object[] row =
                        new Object[columns.length];

                for (
                        int i = 0;
                        i < columns.length;
                        i++
                ) {

                    row[i] =
                            rs.getObject(i + 1);
                }

                rows.add(row);
            }
        }

        return new ReportData(
                columns,
                rows
        );
    }
}