package model;

import java.io.Serializable;

public class Train implements Serializable {
    private static final long serialVersionUID = 1L;

    private String trainId;
    private String trainName;
    private String route;
    private String departureStation;
    private String arrivalStation;
    private String departureTime;
    private String arrivalTime;
    private int totalSeats;
    private int availableSeats;
    private double ticketPrice;
    private String status; // Active, Cancelled, Completed

    public Train(String trainId, String trainName, String route,
                 String departureStation, String arrivalStation,
                 String departureTime, String arrivalTime,
                 int totalSeats, double ticketPrice) {
        this.trainId = trainId;
        this.trainName = trainName;
        this.route = route;
        this.departureStation = departureStation;
        this.arrivalStation = arrivalStation;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.totalSeats = totalSeats;
        this.availableSeats = totalSeats;
        this.ticketPrice = ticketPrice;
        this.status = "Active";
    }

    // Getters and Setters
    public String getTrainId() { return trainId; }
    public void setTrainId(String trainId) { this.trainId = trainId; }
    public String getTrainName() { return trainName; }
    public void setTrainName(String trainName) { this.trainName = trainName; }
    public String getRoute() { return route; }
    public void setRoute(String route) { this.route = route; }
    public String getDepartureStation() { return departureStation; }
    public void setDepartureStation(String departureStation) { this.departureStation = departureStation; }
    public String getArrivalStation() { return arrivalStation; }
    public void setArrivalStation(String arrivalStation) { this.arrivalStation = arrivalStation; }
    public String getDepartureTime() { return departureTime; }
    public void setDepartureTime(String departureTime) { this.departureTime = departureTime; }
    public String getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(String arrivalTime) { this.arrivalTime = arrivalTime; }
    public int getTotalSeats() { return totalSeats; }
    public void setTotalSeats(int totalSeats) { this.totalSeats = totalSeats; }
    public int getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(int availableSeats) { this.availableSeats = availableSeats; }
    public double getTicketPrice() { return ticketPrice; }
    public void setTicketPrice(double ticketPrice) { this.ticketPrice = ticketPrice; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getOccupancyRate() {
        if (totalSeats == 0) return 0;
        return ((double)(totalSeats - availableSeats) / totalSeats) * 100;
    }

    @Override
    public String toString() {
        return trainId + " - " + trainName + " (" + route + ")";
    }
}
