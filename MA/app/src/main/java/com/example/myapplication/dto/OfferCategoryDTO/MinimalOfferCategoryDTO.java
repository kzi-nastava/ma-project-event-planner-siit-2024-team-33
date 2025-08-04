package com.example.myapplication.dto.OfferCategoryDTO;

import com.example.myapplication.models.OfferType;

import java.io.Serializable;

public class MinimalOfferCategoryDTO implements Serializable {
    public Integer id;
    public String name;
    public String description;
    public Boolean isEnabled;
    public OfferType type;
}
