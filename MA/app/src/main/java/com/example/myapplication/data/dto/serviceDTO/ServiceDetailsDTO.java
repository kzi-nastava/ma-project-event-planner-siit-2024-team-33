package com.example.myapplication.data.dto.serviceDTO;

import com.example.myapplication.data.dto.OfferCategoryDTO.MinimalOfferCategoryDTO;
import com.example.myapplication.data.dto.eventTypeDTO.MinimalEventTypeDTO;

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

    public Integer reservationInHours;
    public Integer cancellationInHours;
    public Boolean isAutomatic;
    public Integer minLengthInMins;
    public Integer maxLengthInMins;

    public Integer providerId;
    public String providerName;
    public String providerEmail;

}
