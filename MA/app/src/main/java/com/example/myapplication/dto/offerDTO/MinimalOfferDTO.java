package com.example.myapplication.dto.offerDTO;

import com.example.myapplication.models.OfferType;

public class MinimalOfferDTO {
    private Integer id;
    public Integer offerId;
    private OfferType type;
    private String name;
    private Double basePrice;
    private String description;
    private String images;

    public void setType(OfferType type) {
        this.type = type;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public void setBasePrice(Double basePrice) {
        this.basePrice = basePrice;
    }

    public String getImages() {
        return images;
    }

    public OfferType getType() {
        return type;
    }

    public Double getBasePrice() {
        return basePrice;
    }

    public Integer getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }
}
