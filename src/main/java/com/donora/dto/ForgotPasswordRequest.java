package com.donora.dto;

public class ForgotPasswordRequest {
    private String email;

    // Constructor
    public ForgotPasswordRequest(String email) {
        this.email = email;
    }

    // Getter and Setter
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
