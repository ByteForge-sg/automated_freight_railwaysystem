package model;

public class FreightCar {

    private int wagonId;
    private String wagonType;
    private double capacityTons;
    private String currentStatus;

    public FreightCar() {
    }

    public FreightCar(int wagonId, String wagonType,
                      double capacityTons, String currentStatus) {
        this.wagonId = wagonId;
        this.wagonType = wagonType;
        this.capacityTons = capacityTons;
        this.currentStatus = currentStatus;
    }

    public int getWagonId() {
        return wagonId;
    }

    public void setWagonId(int wagonId) {
        this.wagonId = wagonId;
    }

    public String getWagonType() {
        return wagonType;
    }

    public void setWagonType(String wagonType) {
        this.wagonType = wagonType;
    }

    public double getCapacityTons() {
        return capacityTons;
    }

    public void setCapacityTons(double capacityTons) {
        this.capacityTons = capacityTons;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    @Override
    public String toString() {
        return wagonId + " | " + wagonType + " | "
                + capacityTons + " tons | " + currentStatus;
    }
}