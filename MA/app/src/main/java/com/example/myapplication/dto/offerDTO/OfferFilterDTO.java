package com.example.myapplication.dto.offerDTO;

import com.example.myapplication.dto.eventDTO.MinimalEventTypeDTO;
import com.example.myapplication.models.Availability;

import java.util.ArrayList;
import java.util.List;

public class OfferFilterDTO {
    public Boolean isProduct;
    public Boolean isService;
    public String name;
    public String category;
    public Integer lowestPrice;
    public Availability isAvailable;
    public List<Integer> eventTypes = new ArrayList<>();
}
