package com.example.myapplication.dto.offerDTO;

import com.example.myapplication.dto.eventDTO.MinimalEventTypeDTO;
import com.example.myapplication.models.Availability;

import java.util.List;

public class OfferFilterDTO {
    private Boolean isProduct;
    private Boolean isService;
    private String name;
    private String category;
    private Integer lowestPrice;
    private Availability isAvailable;
    private List<MinimalEventTypeDTO> eventTypes;
}
