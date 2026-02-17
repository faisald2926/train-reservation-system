package controller;

import model.Train;
import util.DataStore;
import java.util.List;

public class TrainController {
    private DataStore dataStore;

    public TrainController() {
        this.dataStore = DataStore.getInstance();
    }

    public String addTrain(String name, String route, String depStation, String arrStation,
                           String depTime, String arrTime, int totalSeats, double price) {
        if (name.isEmpty() || route.isEmpty() || depStation.isEmpty() || arrStation.isEmpty()
                || depTime.isEmpty() || arrTime.isEmpty()) {
            return "All fields are required.";
        }
        if (totalSeats <= 0) return "Total seats must be greater than 0.";
        if (price < 0) return "Price cannot be negative.";

        String id = dataStore.generateTrainId();
        Train train = new Train(id, name, route, depStation, arrStation, depTime, arrTime, totalSeats, price);
        dataStore.addTrain(train);
        return null; // success
    }

    public String updateTrain(String trainId, String name, String route, String depStation,
                              String arrStation, String depTime, String arrTime,
                              int totalSeats, double price) {
        Train train = dataStore.getTrainById(trainId);
        if (train == null) return "Train not found.";
        if (name.isEmpty() || route.isEmpty()) return "Name and route are required.";

        int bookedSeats = train.getTotalSeats() - train.getAvailableSeats();
        if (totalSeats < bookedSeats) {
            return "Cannot set total seats below booked count (" + bookedSeats + ").";
        }

        train.setTrainName(name);
        train.setRoute(route);
        train.setDepartureStation(depStation);
        train.setArrivalStation(arrStation);
        train.setDepartureTime(depTime);
        train.setArrivalTime(arrTime);
        train.setAvailableSeats(totalSeats - bookedSeats);
        train.setTotalSeats(totalSeats);
        train.setTicketPrice(price);
        dataStore.updateTrain(train);
        return null;
    }

    public String deleteTrain(String trainId) {
        Train train = dataStore.getTrainById(trainId);
        if (train == null) return "Train not found.";

        List<model.Reservation> reservations = dataStore.getReservationsByTrain(trainId);
        for (model.Reservation r : reservations) {
            if (r.getStatus() == model.Reservation.Status.CONFIRMED) {
                return "Cannot delete train with active reservations. Cancel them first.";
            }
        }

        dataStore.deleteTrain(trainId);
        return null;
    }

    public List<Train> getAllTrains() {
        return dataStore.getTrains();
    }
}
