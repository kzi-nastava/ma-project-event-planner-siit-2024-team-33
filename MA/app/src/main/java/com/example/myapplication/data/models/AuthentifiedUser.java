package com.example.myapplication.data.models;

import com.example.myapplication.data.models.dto.userDTO.GetUserDTO;

import java.util.Date;
import java.util.List;

public class AuthentifiedUser {
    public Integer id;

    public String email;

    public String password;
    public String name;
    public String surname;
    public String city;
    public Role role;

    public String picture;

    public Date suspensionEndDate;

    public Boolean isDeleted;

    public List<Offer> favoriteOffers;

    public List<Event> favoriteEvents;

    public List<AuthentifiedUser> blockedUsers;

    public List<Notification> notifications;

    public AuthentifiedUser(GetUserDTO dto) {
        this.setEmail(dto.getEmail());
        this.setPicture(dto.getPicture());
        this.setName(dto.getName());
        this.setSurname(dto.getSurname());
        this.setCity(dto.getCity());
        this.setPassword(dto.getPassword());
        if (dto.getRole() != null) {
            Role roleObj = new Role(dto.getRole());
            this.setRole(roleObj);
        } else {
            this.setRole(null);
        }
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setSuspensionEndDate(Date suspensionEndDate) {
        this.suspensionEndDate = suspensionEndDate;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setFavoriteOffers(List<Offer> favoriteOffers) {
        this.favoriteOffers = favoriteOffers;
    }

    public void setFavoriteEvents(List<Event> favoriteEvents) {
        this.favoriteEvents = favoriteEvents;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public List<Offer> getFavoriteOffers() {
        return favoriteOffers;
    }

    public void setBlockedUsers(List<AuthentifiedUser> blockedUsers) {
        this.blockedUsers = blockedUsers;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public List<Event> getFavoriteEvents() {
        return favoriteEvents;
    }

    public Date getSuspensionEndDate() {
        return suspensionEndDate;
    }

    public Integer getId() {
        return id;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public List<AuthentifiedUser> getBlockedUsers() {
        return blockedUsers;
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

    public void setCity(String city) {
        this.city = city;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPicture(String pictures) {
        this.picture = pictures;
    }

    public String getPicture() {
        return picture;
    }
}
