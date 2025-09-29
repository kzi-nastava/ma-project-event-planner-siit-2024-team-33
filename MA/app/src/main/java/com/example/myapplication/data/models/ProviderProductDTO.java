package com.example.myapplication.data.models;

import java.io.Serializable;
import java.util.List;

public class ProviderProductDTO implements Serializable {
    private Integer id;
    private String name;
    private String description;
    private Double price;
    private Double discount;
    private List<String> pictures;
    private Availability availability;
    private Boolean isPending;
    private Boolean isDeleted;
    private Integer offerCategoryId;
    private List<Integer> eventTypeIds;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public Integer getOfferCategoryId() {
        return offerCategoryId;
    }

    public void setOfferCategoryId(Integer offerCategoryId) {
        this.offerCategoryId = offerCategoryId;
    }

    public List<Integer> getEventTypeIds() {
        return eventTypeIds;
    }

    public void setEventTypeIds(List<Integer> eventTypeIds) {
        this.eventTypeIds = eventTypeIds;
    }
}
