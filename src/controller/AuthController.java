package controller;

import model.Passenger;
import model.User;
import util.DataStore;

import java.time.LocalDate;

public class AuthController {
    private DataStore dataStore;

    public AuthController() {
        this.dataStore = DataStore.getInstance();
    }

    public User login(String username, String password) {
        if (username == null || username.trim().isEmpty()) return null;
        if (password == null || password.trim().isEmpty()) return null;
        User user = dataStore.authenticate(username.trim(), password);
        if (user != null) {
            dataStore.setCurrentUser(user);
        }
        return user;
    }

    public void logout() {
        dataStore.setCurrentUser(null);
    }

    public boolean isAdmin() {
        User current = dataStore.getCurrentUser();
        return current != null && current.getRole() == User.Role.ADMIN;
    }

    public String register(String username, String password, String fullName,
                           String email, String phone, String nationalId) {
        if (username == null || username.trim().isEmpty()) return "Username is required.";
        if (password == null || password.length() < 4) return "Password must be at least 4 characters.";
        if (fullName == null || fullName.trim().isEmpty()) return "Full name is required.";
        if (email == null || !email.contains("@") || !email.contains(".")) return "Invalid email format.";
        if (phone == null || phone.trim().isEmpty()) return "Phone is required.";
        if (nationalId == null || nationalId.trim().isEmpty()) return "National ID is required.";

        String uname = username.trim();
        for (User u : dataStore.getUsers()) {
            if (u.getUsername().equalsIgnoreCase(uname)) {
                return "Username already taken.";
            }
        }
        for (Passenger p : dataStore.getPassengers()) {
            if (p.getNationalId().equals(nationalId.trim())) {
                return "A passenger with this National ID already exists.";
            }
        }

        String passengerId = dataStore.generatePassengerId();
        Passenger passenger = new Passenger(passengerId, fullName.trim(), email.trim(),
                phone.trim(), nationalId.trim(), LocalDate.now().toString());
        dataStore.addPassenger(passenger);

        String userId = String.format("USR-%04d", dataStore.getUsers().size() + 1);
        User user = new User(userId, uname, password, User.Role.CUSTOMER, fullName.trim());
        user.setLinkedPassengerId(passengerId);
        dataStore.addUser(user);

        return null;
    }
}
