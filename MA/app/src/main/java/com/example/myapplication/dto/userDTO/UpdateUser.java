package com.example.myapplication.dto.userDTO;

import java.util.List;

public class UpdateUser {
    private String name;
    private String surname;
    private String picture;
    private String residency;
    private String phoneNumber;
    private String description;
    private List<String> pictures;
    private String providerName;
    public UpdateUser() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getSurname() {
        return surname;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public void setPicture(String picture) {
        this.picture = picture;
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

    public void setResidency(String residency) {
        this.residency = residency;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getPicture() {
        return picture;
    }
}
