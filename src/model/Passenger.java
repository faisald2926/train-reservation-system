package model;

import java.io.Serializable;

public class Passenger implements Serializable {
    private static final long serialVersionUID = 1L;

    private String passengerId;
    private String fullName;
    private String email;
    private String phone;
    private String nationalId;
    private String registrationDate;

    public Passenger(String passengerId, String fullName, String email,
                     String phone, String nationalId, String registrationDate) {
        this.passengerId = passengerId;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.nationalId = nationalId;
        this.registrationDate = registrationDate;
    }

    public String getPassengerId() { return passengerId; }
    public void setPassengerId(String passengerId) { this.passengerId = passengerId; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getNationalId() { return nationalId; }
    public void setNationalId(String nationalId) { this.nationalId = nationalId; }
    public String getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(String registrationDate) { this.registrationDate = registrationDate; }

    @Override
    public String toString() {
        return passengerId + " - " + fullName;
    }
}
