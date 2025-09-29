package com.example.myapplication.data.models.dto.userDTO;

import java.util.List;

public class RegisterUser {
    private String email;
    private String password;
    private String name;
    private String surname;
    private String picture;
    private List<String> pictures;
    private String residency;
    private String phoneNumber;
    private String description;
    private String providerName;
    private String role;

    public void setRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public void setResidency(String residency) {
        this.residency = residency;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResidency() {
        return residency;
    }

    public String getProviderName() {
        return providerName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }

    public String getEmail() {
        return email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSurname() {
        return surname;
    }

    public String getPassword() {
        return password;
    }
}
