package util;

import model.Train;
import model.TrackSegment;
import service.HazardousCargoService;
import service.MaintenanceService;
import service.MarshallingYardService;
import service.RecoveryService;
import service.RouteService;
import service.TrainService;
import service.TrackLockService;

import java.util.Arrays;
import java.util.List;

public class TestServices {

    public static void main(String[] args) {

        System.out.println("==================================================");
        System.out.println("   AUTOMATED FREIGHT RAILWAY SIMULATOR");
        System.out.println("             SERVICE LAYER TEST");
        System.out.println("==================================================");

        try {

            // ==================================================
            // 1. TRAIN SERVICE
            // ==================================================

            System.out.println("\n[1] TRAIN SERVICE");

            TrainService trainService = new TrainService();

            List<Train> trains =
                    trainService.getAllTrains();

            for (Train train : trains) {
                System.out.println(train);
            }

            System.out.println(
                    "Total trains: " + trains.size()
            );


            // ==================================================
            // 2. ROUTE SERVICE
            // ==================================================

            System.out.println("\n[2] ROUTE SERVICE");

            RouteService routeService =
                    new RouteService();

            // Find route from Station 1 to Station 8
            List<TrackSegment> route =
                    routeService.findShortestRoute(1, 8);

            if (route.isEmpty()) {

                System.out.println(
                        "No available route found."
                );

            } else {

                System.out.println(
                        "Shortest available route: "
                );

                for (TrackSegment track : route) {
                    System.out.println(track);
                }

                double totalDistance =
                        routeService.calculateRouteDistance(route);

                System.out.println(
                        "Total distance: "
                        + totalDistance
                        + " km"
                );
            }


            // ==================================================
            // 3. TRACK LOCK SERVICE
            // ==================================================

            System.out.println("\n[3] TRACK LOCK SERVICE");

            TrackLockService lockService =
                    new TrackLockService();

            List<Integer> testSegments =
                    Arrays.asList(103);

            boolean locked =
                    lockService.reserveTracks(
                            testSegments,
                            "JAVA_TEST_001"
                    );

            if (locked) {

                System.out.println(
                        "Track 103 successfully locked."
                );

                // Release immediately after demonstration
                lockService.releaseTracks(
                        testSegments,
                        "JAVA_TEST_001"
                );

                System.out.println(
                        "Track 103 released."
                );

            } else {

                System.out.println(
                        "Track 103 could not be locked."
                );
            }


            // ==================================================
            // 4. HAZARDOUS CARGO SERVICE
            // ==================================================

            System.out.println(
                    "\n[4] HAZARDOUS CARGO SERVICE"
            );

            HazardousCargoService hazardousService =
                    new HazardousCargoService();

            List<String> hazardousCargo =
                    hazardousService.getHazardousCargo();

            for (String cargo : hazardousCargo) {
                System.out.println(cargo);
            }

            System.out.println(
                    "Hazardous cargo records: "
                    + hazardousCargo.size()
            );


            // ==================================================
            // 5. MAINTENANCE SERVICE
            // ==================================================

            System.out.println(
                    "\n[5] MAINTENANCE SERVICE"
            );

            MaintenanceService maintenanceService =
                    new MaintenanceService();

            boolean locoAvailable =
                    maintenanceService.canOperate(
                            "LOCOMOTIVE",
                            301
                    );

            System.out.println(
                    "Locomotive 301 operational: "
                    + locoAvailable
            );


            // ==================================================
            // 6. MARSHALLING YARD SERVICE
            // ==================================================

            System.out.println(
                    "\n[6] MARSHALLING YARD SERVICE"
            );

            MarshallingYardService yardService =
                    new MarshallingYardService();

            // Train 703 is AT_YARD in our sample data
            yardService.displayTrainComposition(703);


            // ==================================================
            // 7. RECOVERY SERVICE
            // ==================================================

            System.out.println(
                    "\n[7] RECOVERY SERVICE"
            );

            RecoveryService recoveryService =
                    new RecoveryService();

            recoveryService.displayTransactionLog();


            // ==================================================
            // FINAL RESULT
            // ==================================================

            System.out.println(
                    "\n=================================================="
            );

            System.out.println(
                    "       SERVICE LAYER TEST COMPLETED"
            );

            System.out.println(
                    "=================================================="
            );

        } catch (Exception e) {

            System.out.println(
                    "\n=================================================="
            );

            System.out.println(
                    "             SERVICE TEST FAILED"
            );

            System.out.println(
                    "=================================================="
            );

            e.printStackTrace();
        }
    }
}