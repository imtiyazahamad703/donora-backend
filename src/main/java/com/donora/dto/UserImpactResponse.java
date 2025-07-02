package com.donora.dto;

import java.util.List;

public class UserImpactResponse {
    private int totalDonations;
    private int totalAccepted;
    private int totalRejected;
    private int totalQuantityDonated;
    private List<String> recentItemsDonated;

    // Getters and Setters
    public int getTotalDonations() {
        return totalDonations;
    }

    public void setTotalDonations(int totalDonations) {
        this.totalDonations = totalDonations;
    }

    public int getTotalAccepted() {
        return totalAccepted;
    }

    public void setTotalAccepted(int totalAccepted) {
        this.totalAccepted = totalAccepted;
    }

    public int getTotalRejected() {
        return totalRejected;
    }

    public void setTotalRejected(int totalRejected) {
        this.totalRejected = totalRejected;
    }

    public int getTotalQuantityDonated() {
        return totalQuantityDonated;
    }

    public void setTotalQuantityDonated(int totalQuantityDonated) {
        this.totalQuantityDonated = totalQuantityDonated;
    }

    public List<String> getRecentItemsDonated() {
        return recentItemsDonated;
    }

    public void setRecentItemsDonated(List<String> recentItemsDonated) {
        this.recentItemsDonated = recentItemsDonated;
    }
}
