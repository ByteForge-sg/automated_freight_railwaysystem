package dao;

import model.TrackSegment;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrackSegmentDAO {

    public void addTrackSegment(TrackSegment track) throws SQLException {

        String sql = """
                INSERT INTO TRACK_SEGMENT
                (Segment_ID, From_Station_ID, To_Station_ID,
                 Length_KM, Max_Speed, Status)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, track.getSegmentId());
            ps.setInt(2, track.getFromStationId());
            ps.setInt(3, track.getToStationId());
            ps.setDouble(4, track.getLengthKm());
            ps.setInt(5, track.getMaxSpeed());
            ps.setString(6, track.getStatus());

            ps.executeUpdate();
        }
    }

    public List<TrackSegment> getAllTrackSegments() throws SQLException {

        List<TrackSegment> tracks = new ArrayList<>();

        String sql = """
                SELECT *
                FROM TRACK_SEGMENT
                ORDER BY Segment_ID
                """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                TrackSegment track = new TrackSegment(
                        rs.getInt("Segment_ID"),
                        rs.getInt("From_Station_ID"),
                        rs.getInt("To_Station_ID"),
                        rs.getDouble("Length_KM"),
                        rs.getInt("Max_Speed"),
                        rs.getString("Status")
                );

                tracks.add(track);
            }
        }

        return tracks;
    }

    public void updateStatus(int segmentId, String status)
            throws SQLException {

        String sql = """
                UPDATE TRACK_SEGMENT
                SET Status = ?
                WHERE Segment_ID = ?
                """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, segmentId);

            ps.executeUpdate();
        }
    }
}