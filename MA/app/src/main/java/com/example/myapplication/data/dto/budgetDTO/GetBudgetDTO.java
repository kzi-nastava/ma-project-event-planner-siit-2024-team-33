package com.example.myapplication.data.dto.budgetDTO;

import com.example.myapplication.data.dto.OfferCategoryDTO.MinimalOfferCategoryDTO;

import java.util.List;

public class GetBudgetDTO {
    public Integer eventID;
    public String eventName;
    public List<MinimalOfferCategoryDTO> recommendedOfferTypes;
    public List<BudgetItemDTO> takenItems;
    public List<BudgetOfferDTO> takenOffers;
    public Double maxBudget;
    public Double usedBudget;
}
