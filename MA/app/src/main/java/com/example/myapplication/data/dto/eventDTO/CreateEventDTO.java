package com.example.myapplication.data.dto.eventDTO;

import java.util.HashSet;
import java.util.Set;

public class CreateEventDTO {
    public String name;
    public String description;
    public int numOfAttendees;
    public Boolean isPrivate;
    public String place;
    public String dateOfEvent;
    public String endOfEvent;
    public Integer eventTypeId;

    public Double latitude;
    public Double longitude;

    public Set<CreateEventActivityDTO> eventActivities = new HashSet<>();
    public Set<String> privateInvitations;
}
