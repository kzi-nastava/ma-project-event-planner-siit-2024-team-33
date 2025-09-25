package com.example.myapplication.data.dto.eventDTO;

import java.time.LocalDateTime;
import java.util.Set;

public class CreatedEventDTO {
    private Integer id;
    private String name;
    private String description;
    private int numOfAttendees;
    private Boolean isPrivate;
    private String place;
    private LocalDateTime dateOfEvent;
    private LocalDateTime endOfEvent;
    private Integer eventTypeId;

    private Double latitude;
    private Double longitude;

    private Set<CreatedEventActivityDTO> eventActivities;
}
