package model;

public class TrackSegment {

    private int segmentId;
    private int fromStationId;
    private int toStationId;
    private double lengthKm;
    private int maxSpeed;
    private String status;

    public TrackSegment() {
    }

    public TrackSegment(int segmentId, int fromStationId, int toStationId,
                        double lengthKm, int maxSpeed, String status) {
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

    public void setSegmentId(int segmentId) {
        this.segmentId = segmentId;
    }

    public int getFromStationId() {
        return fromStationId;
    }

    public void setFromStationId(int fromStationId) {
        this.fromStationId = fromStationId;
    }

    public int getToStationId() {
        return toStationId;
    }

    public void setToStationId(int toStationId) {
        this.toStationId = toStationId;
    }

    public double getLengthKm() {
        return lengthKm;
    }

    public void setLengthKm(double lengthKm) {
        this.lengthKm = lengthKm;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return segmentId + " | " + fromStationId + " -> "
                + toStationId + " | " + lengthKm + " km | "
                + maxSpeed + " km/h | " + status;
    }
}