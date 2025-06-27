package com.donora.dto;

import com.donora.enums.Role;

public class RegisterResponse {

    private Long id;
    private String name;
    private String email;
    private Role role;
    private String phone;

    // Constructor
    public RegisterResponse(Long id, String name, String email, String phone, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.role = role;

    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }

    public String getPhone() {
        return phone;
    }

}
