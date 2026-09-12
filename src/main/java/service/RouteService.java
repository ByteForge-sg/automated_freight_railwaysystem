package service;

import dao.TrackSegmentDAO;
import model.TrackSegment;

import java.sql.SQLException;
import java.util.*;

public class RouteService {

    private final TrackSegmentDAO trackDAO;

    public RouteService() {
        this.trackDAO = new TrackSegmentDAO();
    }

    public List<TrackSegment> findShortestRoute(
            int sourceStation,
            int destinationStation) throws SQLException {

        List<TrackSegment> tracks =
                trackDAO.getAllTrackSegments();

        // Graph:
        // Station -> outgoing track segments
        Map<Integer, List<TrackSegment>> graph =
                new HashMap<>();

        for (TrackSegment track : tracks) {

            if (!track.getStatus().equalsIgnoreCase("AVAILABLE")) {
                continue;
            }

            graph.computeIfAbsent(
                    track.getFromStationId(),
                    k -> new ArrayList<>()
            ).add(track);
        }

        // Distance from source
        Map<Integer, Double> distance = new HashMap<>();

        // Previous track used to reach a station
        Map<Integer, TrackSegment> previousTrack =
                new HashMap<>();

        for (Integer station : graph.keySet()) {
            distance.put(station, Double.POSITIVE_INFINITY);
        }

        distance.put(sourceStation, 0.0);

        PriorityQueue<Integer> queue =
                new PriorityQueue<>(
                        Comparator.comparingDouble(
                                station ->
                                        distance.getOrDefault(
                                                station,
                                                Double.POSITIVE_INFINITY
                                        )
                        )
                );

        queue.add(sourceStation);

        Set<Integer> visited = new HashSet<>();

        while (!queue.isEmpty()) {

            int current = queue.poll();

            if (visited.contains(current)) {
                continue;
            }

            visited.add(current);

            if (current == destinationStation) {
                break;
            }

            for (TrackSegment track :
                    graph.getOrDefault(
                            current,
                            Collections.emptyList())) {

                int next = track.getToStationId();

                double newDistance =
                        distance.get(current)
                                + track.getLengthKm();

                if (newDistance <
                        distance.getOrDefault(
                                next,
                                Double.POSITIVE_INFINITY)) {

                    distance.put(next, newDistance);

                    previousTrack.put(next, track);

                    queue.add(next);
                }
            }
        }

        // No route found
        if (!previousTrack.containsKey(destinationStation)
                && sourceStation != destinationStation) {

            return Collections.emptyList();
        }

        // Reconstruct route
        LinkedList<TrackSegment> route =
                new LinkedList<>();

        int current = destinationStation;

        while (current != sourceStation) {

            TrackSegment track =
                    previousTrack.get(current);

            if (track == null) {
                return Collections.emptyList();
            }

            route.addFirst(track);

            current = track.getFromStationId();
        }

        return route;
    }

    public double calculateRouteDistance(
            List<TrackSegment> route) {

        double total = 0;

        for (TrackSegment track : route) {
            total += track.getLengthKm();
        }

        return total;
    }
}