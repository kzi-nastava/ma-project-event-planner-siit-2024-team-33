package com.example.myapplication.models;

public class UpgradeUser {
    private String residency;
    private String phoneNumber;
    private String providerName;
    private String description;
    private String role;

    public UpgradeUser(String trim, String trim1, String s, String s1, String organizerRole) {
        this.residency = trim;
        this.phoneNumber = trim1;
        this.providerName=s;
        this.description=s1;
        this.role=organizerRole;
    }
    public UpgradeUser() {
    }
    // Getters and setters
    public String getResidency() {
        return residency;
    }

    public void setResidency(String residency) {
        this.residency = residency;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
