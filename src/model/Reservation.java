package model;

import java.io.Serializable;

public class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum Status { CONFIRMED, CANCELLED, PENDING }

    private String reservationId;
    private String passengerId;
    private String passengerName;
    private String trainId;
    private String trainName;
    private String bookingDate;
    private String travelDate;
    private int seatCount;
    private double totalPrice;
    private Status status;

    public Reservation(String reservationId, String passengerId, String passengerName,
                       String trainId, String trainName, String bookingDate,
                       String travelDate, int seatCount, double totalPrice) {
        this.reservationId = reservationId;
        this.passengerId = passengerId;
        this.passengerName = passengerName;
        this.trainId = trainId;
        this.trainName = trainName;
        this.bookingDate = bookingDate;
        this.travelDate = travelDate;
        this.seatCount = seatCount;
        this.totalPrice = totalPrice;
        this.status = Status.CONFIRMED;
    }

    public String getReservationId() { return reservationId; }
    public void setReservationId(String reservationId) { this.reservationId = reservationId; }
    public String getPassengerId() { return passengerId; }
    public void setPassengerId(String passengerId) { this.passengerId = passengerId; }
    public String getPassengerName() { return passengerName; }
    public void setPassengerName(String passengerName) { this.passengerName = passengerName; }
    public String getTrainId() { return trainId; }
    public void setTrainId(String trainId) { this.trainId = trainId; }
    public String getTrainName() { return trainName; }
    public void setTrainName(String trainName) { this.trainName = trainName; }
    public String getBookingDate() { return bookingDate; }
    public void setBookingDate(String bookingDate) { this.bookingDate = bookingDate; }
    public String getTravelDate() { return travelDate; }
    public void setTravelDate(String travelDate) { this.travelDate = travelDate; }
    public int getSeatCount() { return seatCount; }
    public void setSeatCount(int seatCount) { this.seatCount = seatCount; }
    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    @Override
    public String toString() {
        return reservationId + " | " + passengerName + " | " + trainName + " | " + status;
    }
}
