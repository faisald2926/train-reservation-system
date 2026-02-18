package controller;

import model.Passenger;
import util.DataStore;
import java.time.LocalDate;
import java.util.List;

public class PassengerController {
    private DataStore dataStore;

    public PassengerController() {
        this.dataStore = DataStore.getInstance();
    }

    public String addPassenger(String name, String email, String phone, String nationalId) {
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || nationalId.isEmpty()) {
            return "All fields are required.";
        }
        if (!email.contains("@") || !email.contains(".")) {
            return "Invalid email format.";
        }

        // Check duplicate national ID
        for (Passenger p : dataStore.getPassengers()) {
            if (p.getNationalId().equals(nationalId)) {
                return "A passenger with this National ID already exists.";
            }
        }

        String id = dataStore.generatePassengerId();
        String regDate = LocalDate.now().toString();
        Passenger passenger = new Passenger(id, name, email, phone, nationalId, regDate);
        dataStore.addPassenger(passenger);
        return null;
    }

    public String updatePassenger(String passengerId, String name, String email,
                                  String phone, String nationalId) {
        Passenger passenger = dataStore.getPassengerById(passengerId);
        if (passenger == null) return "Passenger not found.";
        if (name.isEmpty() || email.isEmpty()) return "Name and email are required.";

        // Check duplicate national ID (excluding current passenger)
        for (Passenger p : dataStore.getPassengers()) {
            if (p.getNationalId().equals(nationalId) && !p.getPassengerId().equals(passengerId)) {
                return "Another passenger with this National ID already exists.";
            }
        }

        passenger.setFullName(name);
        passenger.setEmail(email);
        passenger.setPhone(phone);
        passenger.setNationalId(nationalId);
        dataStore.updatePassenger(passenger);
        return null;
    }

    public String deletePassenger(String passengerId) {
        Passenger passenger = dataStore.getPassengerById(passengerId);
        if (passenger == null) return "Passenger not found.";

        List<model.Reservation> reservations = dataStore.getReservationsByPassenger(passengerId);
        for (model.Reservation r : reservations) {
            if (r.getStatus() == model.Reservation.Status.CONFIRMED) {
                return "Cannot delete passenger with active reservations.";
            }
        }

        dataStore.deletePassenger(passengerId);
        return null;
    }

    public List<Passenger> getAllPassengers() {
        return dataStore.getPassengers();
    }
}
