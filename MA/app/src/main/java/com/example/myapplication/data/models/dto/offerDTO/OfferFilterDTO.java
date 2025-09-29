package com.example.myapplication.data.models.dto.offerDTO;

import com.example.myapplication.data.models.Availability;

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

    public OfferFilterDTO(Boolean isProduct, Boolean isService, String name, String category, Integer lowestPrice, Availability isAvailable, List<Integer> eventTypes){
        this.category = category;
        this.isAvailable=isAvailable;
        this.eventTypes = eventTypes;
        this.lowestPrice=lowestPrice;
        this.name=name;
        this.isService=isService;
        this.isProduct=isProduct;
    }
}
