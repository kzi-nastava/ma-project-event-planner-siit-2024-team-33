package com.example.myapplication.dto.eventDTO;

import com.example.myapplication.models.EventType;
import com.example.myapplication.models.Organizer;

import java.time.LocalDateTime;
import java.util.List;

public class GetEventDTO {
    private String name;
    private String description;
    private int numOfAttendees;
    private Boolean isPrivate;
    private String place;
    private Double latitude;
    private Double longitude;
    private LocalDateTime dateOfEvent;
    private LocalDateTime endOfEvent;
    private Boolean itsJoever;
    private String picture;
    private Integer price;
    private Organizer organizer;
    private List<EventType> eventTypes;

    public Organizer getOrganizer() {
        return organizer;
    }

    public List<EventType> getEventTypes() {
        return eventTypes;
    }

    public String getPlace() {
        return place;
    }

    public LocalDateTime getEndOfEvent() {
        return endOfEvent;
    }

    public Integer getPrice() {
        return price;
    }

    public LocalDateTime getDateOfEvent() {
        return dateOfEvent;
    }

    public int getNumOfAttendees() {
        return numOfAttendees;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Boolean getPrivate() {
        return isPrivate;
    }

    public Boolean getItsJoever() {
        return itsJoever;
    }

    public String getPicture() {
        return picture;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
