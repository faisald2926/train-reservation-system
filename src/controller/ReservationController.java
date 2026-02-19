package controller;

import model.*;
import util.DataStore;
import java.time.LocalDate;
import java.util.List;

public class ReservationController {
    private DataStore dataStore;

    public ReservationController() {
        this.dataStore = DataStore.getInstance();
    }

    public String bookTicket(String passengerId, String trainId, String travelDate, int seatCount) {
        Passenger passenger = dataStore.getPassengerById(passengerId);
        if (passenger == null) return "Passenger not found.";

        Train train = dataStore.getTrainById(trainId);
        if (train == null) return "Train not found.";

        if (!"Active".equals(train.getStatus())) {
            return "This train is not active.";
        }

        if (seatCount <= 0) return "Seat count must be at least 1.";

        if (seatCount > train.getAvailableSeats()) {
            return "Not enough seats available. Only " + train.getAvailableSeats() + " seats left.";
        }

        if (travelDate == null || travelDate.trim().isEmpty()) {
            return "Travel date is required.";
        }

        double totalPrice = train.getTicketPrice() * seatCount;
        String bookingDate = LocalDate.now().toString();
        String resId = dataStore.generateReservationId();

        Reservation reservation = new Reservation(resId, passengerId, passenger.getFullName(),
                trainId, train.getTrainName(), bookingDate, travelDate, seatCount, totalPrice);

        // Update seat availability
        train.setAvailableSeats(train.getAvailableSeats() - seatCount);
        dataStore.updateTrain(train);
        dataStore.addReservation(reservation);

        return null; // success
    }

    public String cancelReservation(String reservationId) {
        Reservation reservation = dataStore.getReservationById(reservationId);
        if (reservation == null) return "Reservation not found.";

        if (reservation.getStatus() == Reservation.Status.CANCELLED) {
            return "This reservation is already cancelled.";
        }

        // Restore seats
        Train train = dataStore.getTrainById(reservation.getTrainId());
        if (train != null) {
            train.setAvailableSeats(train.getAvailableSeats() + reservation.getSeatCount());
            dataStore.updateTrain(train);
        }

        reservation.setStatus(Reservation.Status.CANCELLED);
        dataStore.updateReservation(reservation);
        return null;
    }

    public List<Reservation> getAllReservations() {
        return dataStore.getReservations();
    }

    public List<Reservation> getReservationsByPassenger(String passengerId) {
        return dataStore.getReservationsByPassenger(passengerId);
    }

    public String getBookingConfirmation(String reservationId) {
        Reservation r = dataStore.getReservationById(reservationId);
        if (r == null) return "Reservation not found.";

        StringBuilder sb = new StringBuilder();
        sb.append("===========================================\n");
        sb.append("        BOOKING CONFIRMATION\n");
        sb.append("===========================================\n");
        sb.append("Reservation ID : ").append(r.getReservationId()).append("\n");
        sb.append("Passenger      : ").append(r.getPassengerName()).append("\n");
        sb.append("Train          : ").append(r.getTrainName()).append("\n");
        sb.append("Travel Date    : ").append(r.getTravelDate()).append("\n");
        sb.append("Seats          : ").append(r.getSeatCount()).append("\n");
        sb.append("Total Price    : SAR ").append(String.format("%.2f", r.getTotalPrice())).append("\n");
        sb.append("Booking Date   : ").append(r.getBookingDate()).append("\n");
        sb.append("Status         : ").append(r.getStatus()).append("\n");
        sb.append("===========================================\n");
        return sb.toString();
    }
}
