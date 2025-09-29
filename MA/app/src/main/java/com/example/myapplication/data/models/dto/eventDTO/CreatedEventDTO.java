package com.example.myapplication.data.models.dto.eventDTO;

import java.util.Set;

public class CreatedEventDTO {
    private Integer id;
    private String name;
    private String description;
    private int numOfAttendees;
    private Boolean isPrivate;
    private String place;
    private String dateOfEvent;
    private String endOfEvent;

    private Integer eventTypeId;
    private Double latitude;
    private Double longitude;
    private Set<CreatedEventActivityDTO> eventActivities;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNumOfAttendees() {
        return numOfAttendees;
    }

    public void setNumOfAttendees(int numOfAttendees) {
        this.numOfAttendees = numOfAttendees;
    }

    public Boolean getPrivate() {
        return isPrivate;
    }

    public void setPrivate(Boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getDateOfEvent() {
        return dateOfEvent;
    }

    public void setDateOfEvent(String dateOfEvent) {
        this.dateOfEvent = dateOfEvent;
    }

    public String getEndOfEvent() {
        return endOfEvent;
    }

    public void setEndOfEvent(String endOfEvent) {
        this.endOfEvent = endOfEvent;
    }

    public Integer getEventTypeId() {
        return eventTypeId;
    }

    public void setEventTypeId(Integer eventTypeId) {
        this.eventTypeId = eventTypeId;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Set<CreatedEventActivityDTO> getEventActivities() {
        return eventActivities;
    }

    public void setEventActivities(Set<CreatedEventActivityDTO> eventActivities) {
        this.eventActivities = eventActivities;
    }
}
