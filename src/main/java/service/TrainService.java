package service;

import dao.TrainDAO;
import model.Train;

import java.sql.SQLException;
import java.util.List;

public class TrainService {

    private final TrainDAO trainDAO;

    public TrainService() {
        this.trainDAO = new TrainDAO();
    }

    public void addTrain(Train train) throws SQLException {

        if (train.getTrainNumber() == null ||
                train.getTrainNumber().isBlank()) {

            throw new IllegalArgumentException(
                    "Train number cannot be empty."
            );
        }

        if (train.getTrainType() == null ||
                train.getTrainType().isBlank()) {

            throw new IllegalArgumentException(
                    "Train type cannot be empty."
            );
        }

        trainDAO.addTrain(train);
    }

    public List<Train> getAllTrains() throws SQLException {
        return trainDAO.getAllTrains();
    }

    public void updateTrainStatus(int trainId, String status)
            throws SQLException {

        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException(
                    "Train status cannot be empty."
            );
        }

        trainDAO.updateStatus(trainId, status);
    }

    public void deleteTrain(int trainId) throws SQLException {
        trainDAO.deleteTrain(trainId);
    }
}