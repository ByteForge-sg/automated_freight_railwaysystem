package model;

public class Train {

    private int trainId;
    private String trainNumber;
    private String trainType;
    private String currentStatus;

    public Train() {
    }

    public Train(int trainId, String trainNumber,
                 String trainType, String currentStatus) {
        this.trainId = trainId;
        this.trainNumber = trainNumber;
        this.trainType = trainType;
        this.currentStatus = currentStatus;
    }

    public int getTrainId() {
        return trainId;
    }

    public void setTrainId(int trainId) {
        this.trainId = trainId;
    }

    public String getTrainNumber() {
        return trainNumber;
    }

    public void setTrainNumber(String trainNumber) {
        this.trainNumber = trainNumber;
    }

    public String getTrainType() {
        return trainType;
    }

    public void setTrainType(String trainType) {
        this.trainType = trainType;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    @Override
    public String toString() {
        return trainId + " | " + trainNumber + " | "
                + trainType + " | " + currentStatus;
    }
}