package util;

import model.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataStore {
    private static DataStore instance;
    private static final String DATA_DIR = "data/";

    private List<User> users;
    private List<Train> trains;
    private List<Passenger> passengers;
    private List<Reservation> reservations;
    private User currentUser;

    private int nextTrainId = 1;
    private int nextPassengerId = 1;
    private int nextReservationId = 1;

    private DataStore() {
        users = new ArrayList<>();
        trains = new ArrayList<>();
        passengers = new ArrayList<>();
        reservations = new ArrayList<>();
    }

    public static DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }

    // ===== Current User Session =====
    public User getCurrentUser() { return currentUser; }
    public void setCurrentUser(User user) { this.currentUser = user; }

    // ===== ID Generation =====
    public String generateTrainId() { return "TRN-" + String.format("%04d", nextTrainId++); }
    public String generatePassengerId() { return "PSG-" + String.format("%04d", nextPassengerId++); }
    public String generateReservationId() { return "RES-" + String.format("%04d", nextReservationId++); }

    // ===== Users =====
    public List<User> getUsers() { return users; }
    public void addUser(User user) { users.add(user); saveAll(); }

    public User authenticate(String username, String password) {
        for (User u : users) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }

    // ===== Trains =====
    public List<Train> getTrains() { return trains; }

    public void addTrain(Train train) {
        trains.add(train);
        saveAll();
    }

    public void updateTrain(Train train) {
        for (int i = 0; i < trains.size(); i++) {
            if (trains.get(i).getTrainId().equals(train.getTrainId())) {
                trains.set(i, train);
                break;
            }
        }
        saveAll();
    }

    public void deleteTrain(String trainId) {
        trains.removeIf(t -> t.getTrainId().equals(trainId));
        saveAll();
    }

    public Train getTrainById(String trainId) {
        for (Train t : trains) {
            if (t.getTrainId().equals(trainId)) return t;
        }
        return null;
    }

    // ===== Passengers =====
    public List<Passenger> getPassengers() { return passengers; }

    public void addPassenger(Passenger passenger) {
        passengers.add(passenger);
        saveAll();
    }

    public void updatePassenger(Passenger passenger) {
        for (int i = 0; i < passengers.size(); i++) {
            if (passengers.get(i).getPassengerId().equals(passenger.getPassengerId())) {
                passengers.set(i, passenger);
                break;
            }
        }
        saveAll();
    }

    public void deletePassenger(String passengerId) {
        passengers.removeIf(p -> p.getPassengerId().equals(passengerId));
        saveAll();
    }

    public Passenger getPassengerById(String passengerId) {
        for (Passenger p : passengers) {
            if (p.getPassengerId().equals(passengerId)) return p;
        }
        return null;
    }

    // ===== Reservations =====
    public List<Reservation> getReservations() { return reservations; }

    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
        saveAll();
    }

    public void updateReservation(Reservation reservation) {
        for (int i = 0; i < reservations.size(); i++) {
            if (reservations.get(i).getReservationId().equals(reservation.getReservationId())) {
                reservations.set(i, reservation);
                break;
            }
        }
        saveAll();
    }

    public Reservation getReservationById(String reservationId) {
        for (Reservation r : reservations) {
            if (r.getReservationId().equals(reservationId)) return r;
        }
        return null;
    }

    public List<Reservation> getReservationsByPassenger(String passengerId) {
        List<Reservation> result = new ArrayList<>();
        for (Reservation r : reservations) {
            if (r.getPassengerId().equals(passengerId)) result.add(r);
        }
        return result;
    }

    public List<Reservation> getReservationsByTrain(String trainId) {
        List<Reservation> result = new ArrayList<>();
        for (Reservation r : reservations) {
            if (r.getTrainId().equals(trainId)) result.add(r);
        }
        return result;
    }

    // ===== Statistics =====
    public int getTotalActiveTrains() {
        int count = 0;
        for (Train t : trains) {
            if ("Active".equals(t.getStatus())) count++;
        }
        return count;
    }

    public int getTotalConfirmedReservations() {
        int count = 0;
        for (Reservation r : reservations) {
            if (r.getStatus() == Reservation.Status.CONFIRMED) count++;
        }
        return count;
    }

    public double getTotalRevenue() {
        double total = 0;
        for (Reservation r : reservations) {
            if (r.getStatus() == Reservation.Status.CONFIRMED) {
                total += r.getTotalPrice();
            }
        }
        return total;
    }

    public double getAverageOccupancy() {
        if (trains.isEmpty()) return 0;
        double totalOccupancy = 0;
        for (Train t : trains) {
            totalOccupancy += t.getOccupancyRate();
        }
        return totalOccupancy / trains.size();
    }

    // ===== Persistence =====
    @SuppressWarnings("unchecked")
    public void loadAll() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
            createDefaultUsers();
            saveAll();
            return;
        }

        users = loadList(DATA_DIR + "users.dat");
        trains = loadList(DATA_DIR + "trains.dat");
        passengers = loadList(DATA_DIR + "passengers.dat");
        reservations = loadList(DATA_DIR + "reservations.dat");

        // Load counters
        try (BufferedReader br = new BufferedReader(new FileReader(DATA_DIR + "counters.dat"))) {
            nextTrainId = Integer.parseInt(br.readLine().trim());
            nextPassengerId = Integer.parseInt(br.readLine().trim());
            nextReservationId = Integer.parseInt(br.readLine().trim());
        } catch (Exception e) {
            recalculateCounters();
        }

        if (users.isEmpty()) {
            createDefaultUsers();
            saveAll();
        }
    }

    public void saveAll() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) dir.mkdirs();

        saveList(users, DATA_DIR + "users.dat");
        saveList(trains, DATA_DIR + "trains.dat");
        saveList(passengers, DATA_DIR + "passengers.dat");
        saveList(reservations, DATA_DIR + "reservations.dat");

        // Save counters
        try (PrintWriter pw = new PrintWriter(new FileWriter(DATA_DIR + "counters.dat"))) {
            pw.println(nextTrainId);
            pw.println(nextPassengerId);
            pw.println(nextReservationId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private <T> List<T> loadList(String filename) {
        File file = new File(filename);
        if (!file.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<T>) ois.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private <T> void saveList(List<T> list, String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createDefaultUsers() {
        users.add(new User("USR-0001", "admin", "admin123", User.Role.ADMIN, "System Administrator"));
        users.add(new User("USR-0002", "staff", "staff123", User.Role.STAFF, "Staff Member"));
    }

    private void recalculateCounters() {
        nextTrainId = trains.size() + 1;
        nextPassengerId = passengers.size() + 1;
        nextReservationId = reservations.size() + 1;
    }
}
