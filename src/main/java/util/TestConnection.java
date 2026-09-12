package util;

import java.sql.Connection;

public class TestConnection {

    public static void main(String[] args) {

        try (Connection connection = DatabaseConnection.getConnection()) {

            System.out.println("=================================");
            System.out.println("DATABASE CONNECTION SUCCESSFUL");
            System.out.println("Database: freight_railway_db");
            System.out.println("=================================");

        } catch (Exception e) {

            System.out.println("DATABASE CONNECTION FAILED");
            e.printStackTrace();
        }
    }
}