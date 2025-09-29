package com.example.myapplication.data.models.dto.OfferCategoryDTO;

import com.example.myapplication.data.models.OfferType;

import java.io.Serializable;

public class MinimalOfferCategoryDTO implements Serializable {
    public Integer id;
    public String name;
    public String description;
    public Boolean isEnabled;
    public OfferType type;
}
