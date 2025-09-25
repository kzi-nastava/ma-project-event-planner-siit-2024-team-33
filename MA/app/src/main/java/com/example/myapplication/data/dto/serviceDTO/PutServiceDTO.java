package com.example.myapplication.data.dto.serviceDTO;

import com.example.myapplication.data.models.Availability;

import java.util.List;

public class PutServiceDTO {
    public String name;
    public Double price;
    public String description;
    public Double discount;
    public Integer reservationInHours;
    public Integer cancellationInHours;
    public Integer minDurationInMins;
    public Integer maxDurationInMins;
    public Availability availability;
    public List<String> picturesDataURI;
    public Boolean isAutomatic;

    public List<Integer> validEventTypeIDs;
}
