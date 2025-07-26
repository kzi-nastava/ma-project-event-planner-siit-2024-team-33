package com.example.myapplication.dto.productDTO;

import com.example.myapplication.dto.OfferCategoryDTO.MinimalOfferCategoryDTO;
import com.example.myapplication.dto.eventTypeDTO.MinimalEventTypeDTO;
import com.example.myapplication.models.Availability;

import java.util.List;

public class GetProductDTO {
    public Integer versionId;
    public Integer offerId;
    public String name;
    public String description;
    public Double price;
    public Double discount;
    public List<String> picturesDataURI;
    public Availability availability;
    public MinimalOfferCategoryDTO category;

    public Integer providerId;
    public String providerEmail;
    public String providerName;

    public List<MinimalEventTypeDTO> validEvents;
}
