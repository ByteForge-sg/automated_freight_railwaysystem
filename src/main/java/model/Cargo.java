package model;

public class Cargo {

    private int cargoId;
    private String description;
    private double weightTons;
    private String hazardClass;

    public Cargo() {
    }

    public Cargo(int cargoId, String description,
                 double weightTons, String hazardClass) {
        this.cargoId = cargoId;
        this.description = description;
        this.weightTons = weightTons;
        this.hazardClass = hazardClass;
    }

    public int getCargoId() {
        return cargoId;
    }

    public void setCargoId(int cargoId) {
        this.cargoId = cargoId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getWeightTons() {
        return weightTons;
    }

    public void setWeightTons(double weightTons) {
        this.weightTons = weightTons;
    }

    public String getHazardClass() {
        return hazardClass;
    }

    public void setHazardClass(String hazardClass) {
        this.hazardClass = hazardClass;
    }

    @Override
    public String toString() {
        return cargoId + " | " + description + " | "
                + weightTons + " tons | " + hazardClass;
    }
}