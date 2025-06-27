package com.donora.dto;

public class ResetPasswordRequest {
    private String otp;
    private String newPassword;

    // Constructor
    public ResetPasswordRequest(String otp, String newPassword) {
        this.otp = otp;
        this.newPassword = newPassword;
    }

    // Getter and Setter
    public String getOtp() {
        return otp;
    }

    public void setToken(String otp) {
        this.otp = otp;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
