package model;

public class Locomotive {

    private int locoId;
    private String model;
    private int powerRatingHp;
    private String status;

    public Locomotive() {
    }

    public Locomotive(int locoId, String model,
                      int powerRatingHp, String status) {
        this.locoId = locoId;
        this.model = model;
        this.powerRatingHp = powerRatingHp;
        this.status = status;
    }

    public int getLocoId() {
        return locoId;
    }

    public void setLocoId(int locoId) {
        this.locoId = locoId;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getPowerRatingHp() {
        return powerRatingHp;
    }

    public void setPowerRatingHp(int powerRatingHp) {
        this.powerRatingHp = powerRatingHp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return locoId + " | " + model + " | "
                + powerRatingHp + " HP | " + status;
    }
}