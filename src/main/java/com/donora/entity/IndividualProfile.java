package com.donora.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "individual_profiles")
public class IndividualProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String address;
    private String city;
    private String state;
    private String pinCode;
    private String occupation;
    private String gender;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    // Getters and Setters (no Lombok)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getPinCode() { return pinCode; }
    public void setPinCode(String pinCode) { this.pinCode = pinCode; }

    public String getOccupation() { return occupation; }
    public void setOccupation(String occupation) { this.occupation = occupation; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
