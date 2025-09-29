package com.example.myapplication.data.models.dto;

import com.example.myapplication.data.models.Availability;
import com.example.myapplication.data.models.dto.OfferCategoryDTO.PostOfferCategoryDTO;

import java.util.List;

public class CreateProductDTO {
    private String name;
    private String description;
    private Double price;
    private Double discount;
    private List<String> pictures;
    private Availability availability;
    private Boolean isPending;
    private Integer existingCategoryId;
    private PostOfferCategoryDTO newCategory;
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

    public Boolean getPending() {
        return isPending;
    }

    public void setPending(Boolean pending) {
        isPending = pending;
    }

    public Integer getExistingCategoryId() {
        return existingCategoryId;
    }

    public void setExistingCategoryId(Integer existingCategoryId) {
        this.existingCategoryId = existingCategoryId;
    }

    public PostOfferCategoryDTO getNewCategory() {
        return newCategory;
    }

    public void setNewCategory(PostOfferCategoryDTO newCategory) {
        this.newCategory = newCategory;
    }

    public List<Integer> getEventTypeIds() {
        return eventTypeIds;
    }

    public void setEventTypeIds(List<Integer> eventTypeIds) {
        this.eventTypeIds = eventTypeIds;
    }
}
