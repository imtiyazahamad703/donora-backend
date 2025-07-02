package com.donora.dto.kafka;

public class FoodDonationStatusKafkaMessage {
    private String donorEmail;
    private String foodName;
    private int quantity;
    private String newStatus;

    // Getters and Setters
    public String getDonorEmail() {
        return donorEmail;
    }
    public void setDonorEmail(String donorEmail) {
        this.donorEmail = donorEmail;
    }
    public String getFoodName() {
        return foodName;
    }
    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public String getNewStatus() {
        return newStatus;
    }
    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus;
    }
}
