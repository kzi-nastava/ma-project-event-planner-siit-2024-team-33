package com.example.myapplication.models;

import java.util.Date;
import java.util.List;

public class AuthentifiedUser {
    public Integer id;

    public String email;

    public String password;
    public String name;
    public String surname;
    public String city;

    public List<String> pictures;

    public Date suspensionEndDate;

    public Boolean isDeleted;

    public List<Offer> favoriteOffers;

    public List<Event> favoriteEvents;

    public List<AuthentifiedUser> blockedUsers;

    public List<Notification> notifications;
}
