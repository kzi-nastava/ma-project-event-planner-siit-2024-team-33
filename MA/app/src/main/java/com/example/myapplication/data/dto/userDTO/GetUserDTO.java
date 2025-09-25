package com.example.myapplication.data.dto.userDTO;

import com.example.myapplication.data.models.Admin;
import com.example.myapplication.data.models.AuthentifiedUser;
import com.example.myapplication.data.models.Organizer;
import com.example.myapplication.data.models.Provider;

import java.util.List;

public class GetUserDTO {
    private String email;
    private String password;
    private String name;
    private String surname;
    private String city;
    private String picture;
    private String residency;
    private String phoneNumber;
    private String providerName;
    private String description;
    private List<String> pictures;
    private String roleName;

    public String getRole() { return roleName; }
    public void setRole(String role) { this.roleName = role; }

    public static GetUserDTO from(AuthentifiedUser user){
        if (user instanceof Provider) return new GetUserDTO((Provider) user);
        if (user instanceof Organizer) return new GetUserDTO((Organizer) user);
        if (user instanceof Admin) return new GetUserDTO((Admin) user);
        return new GetUserDTO(user);
    }

    public GetUserDTO() {
    }

    public GetUserDTO(AuthentifiedUser au) {
        this.setEmail(au.getEmail());
        this.setPassword(au.getPassword());
        this.setName(au.getName());
        this.setSurname(au.getSurname());
        this.setCity(au.getCity());
        this.setPicture(au.getPicture());
        this.setRole(au.getRole().getName());
    }

    public GetUserDTO(Admin admin) {
        this.setEmail(admin.getEmail());
        this.setPassword(admin.getPassword());
        this.setName(admin.getName());
        this.setSurname(admin.getSurname());
        this.setCity(admin.getCity());
        this.setPicture(admin.getPicture());
        this.setRole(admin.getRole().getName());
    }

    public GetUserDTO(Organizer organizer) {
        this.setEmail(organizer.getEmail());
        this.setPassword(organizer.getPassword());
        this.setName(organizer.getName());
        this.setSurname(organizer.getSurname());
        this.setCity(organizer.getCity());
        this.setPicture(organizer.getPicture());
        this.setResidency(organizer.getResidency());
        this.setPhoneNumber(organizer.getPhoneNumber());
        this.setRole(organizer.getRole().getName());

    }

    public GetUserDTO(Provider provider) {
        this.setEmail(provider.getEmail());
        this.setPassword(provider.getPassword());
        this.setName(provider.getName());
        this.setSurname(provider.getSurname());
        this.setCity(provider.getCity());
        this.setPicture(provider.getPicture());
        this.setResidency(provider.getResidency());
        this.setPhoneNumber(provider.getPhoneNumber());
        this.setProviderName(provider.getProviderName());
        this.setDescription(provider.getDescription());
        this.setPictures(provider.getPictures());
        this.setRole(provider.getRole().getName());
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setResidency(String residency) {
        this.residency = residency;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public String getEmail() {
        return email;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPicture() {
        return picture;
    }

    public String getProviderName() {
        return providerName;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public String getResidency() {
        return residency;
    }

    public String getSurname() {
        return surname;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}
