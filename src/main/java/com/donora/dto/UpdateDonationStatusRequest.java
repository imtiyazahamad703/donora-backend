package com.donora.dto;

public class UpdateDonationStatusRequest {

    private String status; // ACCEPTED or REJECTED

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
