package util;

import dao.StationDAO;
import dao.TrackSegmentDAO;
import dao.TrainDAO;
import model.Station;
import model.TrackSegment;
import model.Train;

import java.util.List;

public class TestDAO {

    public static void main(String[] args) {

        try {

            System.out.println("========================================");
            System.out.println("   AUTOMATED FREIGHT RAILWAY SIMULATOR");
            System.out.println("          DAO DATABASE TEST");
            System.out.println("========================================");

            // -------------------------------
            // TEST STATION DAO
            // -------------------------------

            StationDAO stationDAO = new StationDAO();

            List<Station> stations = stationDAO.getAllStations();

            System.out.println("\n--- STATIONS ---");

            for (Station station : stations) {
                System.out.println(station);
            }

            System.out.println("Total stations: " + stations.size());


            // -------------------------------
            // TEST TRACK DAO
            // -------------------------------

            TrackSegmentDAO trackDAO = new TrackSegmentDAO();

            List<TrackSegment> tracks = trackDAO.getAllTrackSegments();

            System.out.println("\n--- TRACK SEGMENTS ---");

            for (TrackSegment track : tracks) {
                System.out.println(track);
            }

            System.out.println("Total track segments: " + tracks.size());


            // -------------------------------
            // TEST TRAIN DAO
            // -------------------------------

            TrainDAO trainDAO = new TrainDAO();

            List<Train> trains = trainDAO.getAllTrains();

            System.out.println("\n--- TRAINS ---");

            for (Train train : trains) {
                System.out.println(train);
            }

            System.out.println("Total trains: " + trains.size());


            // -------------------------------
            // FINAL RESULT
            // -------------------------------

            System.out.println("\n========================================");
            System.out.println("       DAO TEST COMPLETED SUCCESSFULLY");
            System.out.println("========================================");

        } catch (Exception e) {

            System.out.println("\n========================================");
            System.out.println("             DAO TEST FAILED");
            System.out.println("========================================");

            e.printStackTrace();
        }
    }
}