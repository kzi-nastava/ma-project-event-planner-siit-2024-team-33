package com.example.myapplication.dto.serviceDTO;

import com.example.myapplication.dto.OfferCategoryDTO.MinimalOfferCategoryDTO;
import com.example.myapplication.dto.eventTypeDTO.MinimalEventTypeDTO;

import java.io.Serializable;
import java.util.List;

public class ServiceDetailsDTO implements Serializable {
    public Integer versionId;
    public Integer offerId;
    public MinimalOfferCategoryDTO category;
    public String name;
    public String description;
    public Double price;
    public Double discount;
    public List<String> picturesDataURI;
    public List<MinimalEventTypeDTO> validEventCategories;
    public Double avgRating;
    public Boolean isVisible;
    public Boolean isAvailable;

    public int reservationInHours;
    public int cancellationInHours;
    public Boolean isAutomatic;
    public int minLengthInMins;
    public int maxLengthInMins;

    public int providerId;
    public String providerName;
    public String providerEmail;

}
