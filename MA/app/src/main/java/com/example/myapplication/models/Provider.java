package com.example.myapplication.models;

import java.util.List;

public class Provider extends AuthentifiedUser {
    private String residency;
    private String phoneNumber;
    private String providerName;
    private String description;
    private List<String> pictures;


    public List<String> getPictures() {
        return pictures;
    }

    public String getDescription() {
        return description;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getProviderName() {
        return providerName;
    }

    public String getResidency() {
        return residency;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }

    public void setResidency(String residency) {
        this.residency = residency;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }
}
