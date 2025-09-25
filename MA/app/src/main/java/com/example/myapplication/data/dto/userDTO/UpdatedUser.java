package com.example.myapplication.data.dto.userDTO;

import com.example.myapplication.data.models.AuthentifiedUser;
import com.example.myapplication.data.models.Organizer;
import com.example.myapplication.data.models.Provider;

import java.util.List;

public class UpdatedUser {
    private String email;
    private String name;
    private String surname;
    private String picture;
    private String city;
    private String residency;
    private String phoneNumber;
    private String description;
    private String providerName;
    private List<String> pictures;

    public UpdatedUser() {
    }

    public String getCity() {
        return city;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPicture() {
        return picture;
    }

    public String getResidency() {
        return residency;
    }

    public String getProviderName() {
        return providerName;
    }

    public String getDescription() {
        return description;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public String getSurname() {
        return surname;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setName(String name) {
        this.name = name;
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

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }

    public UpdatedUser(AuthentifiedUser aUser) {
        this.setEmail(aUser.getEmail());
        this.setName(aUser.getName());
        this.setSurname(aUser.getSurname());
        this.setPicture(aUser.getPicture());
    }

    public UpdatedUser(Organizer organizer) {
        this.setEmail(organizer.getEmail());
        this.setName(organizer.getName());
        this.setSurname(organizer.getSurname());
        this.setPicture(organizer.getPicture());
        this.setCity(organizer.getCity());
        this.setResidency(organizer.getResidency());
        this.setPhoneNumber(organizer.getPhoneNumber());
    }

    public UpdatedUser(Provider provider) {
        this.setEmail(provider.getEmail());
        this.setName(provider.getName());
        this.setSurname(provider.getSurname());
        this.setPicture(provider.getPicture());
        this.setCity(provider.getCity());
        this.setResidency(provider.getResidency());
        this.setPhoneNumber(provider.getPhoneNumber());
        this.setDescription(provider.getDescription());
        this.setProviderName(provider.getProviderName());
        this.setPictures(provider.getPictures());
    }
}
