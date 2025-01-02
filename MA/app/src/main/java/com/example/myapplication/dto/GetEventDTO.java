package com.example.myapplication.dto;

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
}
