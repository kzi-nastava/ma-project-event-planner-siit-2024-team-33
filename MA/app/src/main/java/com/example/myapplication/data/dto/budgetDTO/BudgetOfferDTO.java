package com.example.myapplication.data.dto.budgetDTO;

import com.example.myapplication.data.models.OfferType;

import java.io.Serializable;

public class BudgetOfferDTO implements Serializable {
    public Integer versionId;
    public Integer categoryId;
    public Double cost;
    public String name;
    public String description;
    public OfferType type;
}
