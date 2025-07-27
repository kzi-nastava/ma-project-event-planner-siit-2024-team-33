package com.example.myapplication.dto.budgetDTO;

import com.example.myapplication.models.OfferType;

import java.io.Serializable;

public class BudgetOfferDTO implements Serializable {
    public Integer versionId;
    public Integer categoryId;
    public Double cost;
    public String name;
    public String description;
    public OfferType type;
}
