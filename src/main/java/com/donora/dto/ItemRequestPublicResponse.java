package com.donora.dto;

import com.donora.enums.UrgencyLevel;

import java.time.LocalDateTime;

public class ItemRequestPublicResponse {

    private Long id;
    private String itemName;
    private String description;
    private Integer quantity;
    private UrgencyLevel urgencyLevel;
    private boolean emergency;
    private LocalDateTime createdAt;

    private String ngoName;
    private String ngoCity;
    private String ngoState;
    private String ngoFocusArea;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public UrgencyLevel getUrgencyLevel() {
        return urgencyLevel;
    }

    public void setUrgencyLevel(UrgencyLevel urgencyLevel) {
        this.urgencyLevel = urgencyLevel;
    }

    public boolean isEmergency() {
        return emergency;
    }

    public void setEmergency(boolean emergency) {
        this.emergency = emergency;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getNgoName() {
        return ngoName;
    }

    public void setNgoName(String ngoName) {
        this.ngoName = ngoName;
    }

    public String getNgoCity() {
        return ngoCity;
    }

    public void setNgoCity(String ngoCity) {
        this.ngoCity = ngoCity;
    }

    public String getNgoState() {
        return ngoState;
    }

    public void setNgoState(String ngoState) {
        this.ngoState = ngoState;
    }

    public String getNgoFocusArea() {
        return ngoFocusArea;
    }

    public void setNgoFocusArea(String ngoFocusArea) {
        this.ngoFocusArea = ngoFocusArea;
    }
}
