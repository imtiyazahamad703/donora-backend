package com.donora.entity;

import com.donora.enums.Role;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(unique = true)
    private String email;
    private String password;
    private String phone;
    private Double locationLat;
    private Double locationLong;
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private NgoProfile ngoProfile;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private BusinessProfile businessProfile;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private IndividualProfile individualProfile;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Double getLocationLat() {
        return locationLat;
    }

    public void setLocationLat(Double locationLat) {
        this.locationLat = locationLat;
    }

    public Double getLocationLong() {
        return locationLong;
    }

    public void setLocationLong(Double locationLong) {
        this.locationLong = locationLong;
    }

    public NgoProfile getNgoProfile() {
        return ngoProfile;
    }

    public void setNgoProfile(NgoProfile ngoProfile) {
        this.ngoProfile = ngoProfile;
    }

    public BusinessProfile getBusinessProfile() {
        return businessProfile;
    }

    public void setBusinessProfile(BusinessProfile businessProfile) {
        this.businessProfile = businessProfile;
    }

    public IndividualProfile getIndividualProfile() {
        return individualProfile;
    }

    public void setIndividualProfile(IndividualProfile individualProfile) {
        this.individualProfile = individualProfile;
    }
}
