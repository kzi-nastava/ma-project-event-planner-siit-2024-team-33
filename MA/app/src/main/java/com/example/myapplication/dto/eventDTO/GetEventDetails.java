package com.example.myapplication.dto.eventDTO;

import java.time.LocalDateTime;

public class GetEventDetails {
    private int id;
    private String name;
    private String description;
    private int numOfAttendees;
    private int numOfCurrentlyApplied;
    private String place;
    private LocalDateTime dateOfEvent;
    private LocalDateTime endOfEvent;
    private double latitude;
    private double longitude;
    private MinimalOrganizerDTO minimalOrganizer;
    private MinimalEventTypeDTO minimalEventType;

    // Getters and setters

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getNumOfAttendees() { return numOfAttendees; }
    public void setNumOfAttendees(int numOfAttendees) { this.numOfAttendees = numOfAttendees; }

    public int getNumOfCurrentlyApplied() { return numOfCurrentlyApplied; }
    public void setNumOfCurrentlyApplied(int numOfCurrentlyApplied) { this.numOfCurrentlyApplied = numOfCurrentlyApplied; }

    public String getPlace() { return place; }
    public void setPlace(String place) { this.place = place; }

    public LocalDateTime getDateOfEvent() { return dateOfEvent; }
    public void setDateOfEvent(LocalDateTime dateOfEvent) { this.dateOfEvent = dateOfEvent; }

    public LocalDateTime getEndOfEvent() { return endOfEvent; }
    public void setEndOfEvent(LocalDateTime endOfEvent) { this.endOfEvent = endOfEvent; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public MinimalOrganizerDTO getMinimalOrganizer() { return minimalOrganizer; }
    public void setMinimalOrganizer(MinimalOrganizerDTO minimalOrganizer) { this.minimalOrganizer = minimalOrganizer; }

    public MinimalEventTypeDTO getMinimalEventType() { return minimalEventType; }
    public void setMinimalEventType(MinimalEventTypeDTO minimalEventType) { this.minimalEventType = minimalEventType; }

    // Nested DTO classes:

    public static class MinimalOrganizerDTO {
        private String email;
        private String name;
        private String surname;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getSurname() { return surname; }
        public void setSurname(String surname) { this.surname = surname; }
    }

    public static class MinimalEventTypeDTO {
        private String name;
        private String description;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }
}
