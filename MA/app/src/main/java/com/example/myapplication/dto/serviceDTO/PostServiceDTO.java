package com.example.myapplication.dto.serviceDTO;

import com.example.myapplication.dto.OfferCategoryDTO.MinimalOfferCategoryDTO;
import com.example.myapplication.dto.eventTypeDTO.MinimalEventTypeDTO;
import com.example.myapplication.models.Availability;

import java.util.List;

public class PostServiceDTO {
    //DTO Will either contain this
    public Integer categoryID;
    //or these
    public String categoryName;
    public String categoryDescription;

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
