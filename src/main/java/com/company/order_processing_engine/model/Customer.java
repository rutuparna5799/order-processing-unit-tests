package com.company.order_processing_engine.model;

public class Customer {
    private String customerId;
    private boolean active;
    private boolean premium;

    public Customer() {
    }

    // All-args constructor
    public Customer(String customerId, boolean active, boolean premium) {
        this.customerId = customerId;
        this.active = active;
        this.premium = premium;
    }

    // Getters & Setters

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isPremium() {
        return premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }

}
