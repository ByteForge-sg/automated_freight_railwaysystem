package service;

import util.DatabaseConnection;

import java.sql.*;

public class RecoveryService {

    public void displayTransactionLog()
            throws SQLException {

        String sql = """
                SELECT
                    Log_ID,
                    Transaction_ID,
                    Operation,
                    Table_Name,
                    Record_ID,
                    Old_Value,
                    New_Value,
                    Log_Time,
                    Status
                FROM TRANSACTION_LOG
                ORDER BY Log_ID
                """;

        try (Connection con =
                     DatabaseConnection.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(sql);
             ResultSet rs =
                     ps.executeQuery()) {

            System.out.println(
                    "\nTRANSACTION RECOVERY LOG"
            );

            while (rs.next()) {

                System.out.println(
                        "Log " +
                        rs.getInt("Log_ID") +
                        " | TXN: " +
                        rs.getString("Transaction_ID") +
                        " | Operation: " +
                        rs.getString("Operation") +
                        " | Table: " +
                        rs.getString("Table_Name") +
                        " | Record: " +
                        rs.getString("Record_ID") +
                        " | Old: " +
                        rs.getString("Old_Value") +
                        " | New: " +
                        rs.getString("New_Value") +
                        " | Status: " +
                        rs.getString("Status")
                );
            }
        }
    }
}