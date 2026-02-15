package controller;

import model.User;
import util.DataStore;

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
}
