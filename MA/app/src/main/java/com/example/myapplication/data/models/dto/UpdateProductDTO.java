package com.example.myapplication.data.models.dto;

import com.example.myapplication.data.models.Availability;

import java.util.List;

public class UpdateProductDTO {
    private String name;
    private String description;
    private Double price;
    private Double discount;
    private List<String> pictures;
    private Availability availability;
    private List<Integer> eventTypeIds;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }

    public Availability getAvailability() {
        return availability;
    }

    public void setAvailability(Availability availability) {
        this.availability = availability;
    }

    public List<Integer> getEventTypeIds() {
        return eventTypeIds;
    }

    public void setEventTypeIds(List<Integer> eventTypeIds) {
        this.eventTypeIds = eventTypeIds;
    }
}
