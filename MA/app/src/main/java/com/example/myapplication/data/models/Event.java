package com.example.myapplication.data.models;

import java.time.LocalDateTime;

public class Event {
    public Integer id;
    public String name;
    public String description;
    public int numOfAttendees;
    public Boolean isPrivate;
    public String place;
    public Double latitude;
    public Double longitude;
    public LocalDateTime dateOfEvent;
    public LocalDateTime endOfEvent;
    public Boolean itsJoever;
    public String picture;
    public Integer price;
}
