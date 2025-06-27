package com.donora.dto;

import java.util.List;

public class NgoImpactStatsResponse {

    private int totalAcceptedItemDonations;
    private int totalAcceptedFoodDonations;

    // Optional: estimation based on quantities
    private int totalItemsHelped;
    private int totalFoodServingsHelped;

    private List<String> recentAcceptedItems; // e.g., ["Winter Jacket", "Books"]
    private List<String> recentAcceptedFoods; // e.g., ["Veg Biryani", "Sandwiches"]

    public int getTotalAcceptedItemDonations() {
        return totalAcceptedItemDonations;
    }

    public void setTotalAcceptedItemDonations(int totalAcceptedItemDonations) {
        this.totalAcceptedItemDonations = totalAcceptedItemDonations;
    }

    public int getTotalAcceptedFoodDonations() {
        return totalAcceptedFoodDonations;
    }

    public void setTotalAcceptedFoodDonations(int totalAcceptedFoodDonations) {
        this.totalAcceptedFoodDonations = totalAcceptedFoodDonations;
    }

    public int getTotalItemsHelped() {
        return totalItemsHelped;
    }

    public void setTotalItemsHelped(int totalItemsHelped) {
        this.totalItemsHelped = totalItemsHelped;
    }

    public int getTotalFoodServingsHelped() {
        return totalFoodServingsHelped;
    }

    public void setTotalFoodServingsHelped(int totalFoodServingsHelped) {
        this.totalFoodServingsHelped = totalFoodServingsHelped;
    }

    public List<String> getRecentAcceptedItems() {
        return recentAcceptedItems;
    }

    public void setRecentAcceptedItems(List<String> recentAcceptedItems) {
        this.recentAcceptedItems = recentAcceptedItems;
    }

    public List<String> getRecentAcceptedFoods() {
        return recentAcceptedFoods;
    }

    public void setRecentAcceptedFoods(List<String> recentAcceptedFoods) {
        this.recentAcceptedFoods = recentAcceptedFoods;
    }
}
