package model;

public class Station {

    private int stationId;
    private String stationName;
    private String location;
    private String stationType;

    public Station() {
    }

    public Station(int stationId, String stationName, String location, String stationType) {
        this.stationId = stationId;
        this.stationName = stationName;
        this.location = location;
        this.stationType = stationType;
    }

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStationType() {
        return stationType;
    }

    public void setStationType(String stationType) {
        this.stationType = stationType;
    }

    @Override
    public String toString() {
        return stationId + " | " + stationName + " | "
                + location + " | " + stationType;
    }
}