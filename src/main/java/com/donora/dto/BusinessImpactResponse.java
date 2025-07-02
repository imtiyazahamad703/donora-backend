package com.donora.dto;

import java.util.List;

public class BusinessImpactResponse {
    private int totalFoodDonations;
    private int totalQuantityDonated;
    private int uniqueNgosHelped;
    private List<String> recentAcceptedFoods;

    // Getters and Setters
    public int getTotalFoodDonations() {
        return totalFoodDonations;
    }

    public void setTotalFoodDonations(int totalFoodDonations) {
        this.totalFoodDonations = totalFoodDonations;
    }

    public int getTotalQuantityDonated() {
        return totalQuantityDonated;
    }

    public void setTotalQuantityDonated(int totalQuantityDonated) {
        this.totalQuantityDonated = totalQuantityDonated;
    }

    public int getUniqueNgosHelped() {
        return uniqueNgosHelped;
    }

    public void setUniqueNgosHelped(int uniqueNgosHelped) {
        this.uniqueNgosHelped = uniqueNgosHelped;
    }

    public List<String> getRecentAcceptedFoods() {
        return recentAcceptedFoods;
    }

    public void setRecentAcceptedFoods(List<String> recentAcceptedFoods) {
        this.recentAcceptedFoods = recentAcceptedFoods;
    }
}
