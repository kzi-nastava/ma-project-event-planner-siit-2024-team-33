package com.example.myapplication.data.models;

import java.util.HashSet;
import java.util.Set;

public class UpdateEventTypeDTO {
    private String description;
    private Set<Integer> recommendedCategoriesIds = new HashSet<>();

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Integer> getRecommendedCategoriesIds() {
        return recommendedCategoriesIds;
    }

    public void setRecommendedCategoriesIds(Set<Integer> recommendedCategoriesIds) {
        this.recommendedCategoriesIds = recommendedCategoriesIds;
    }
}
