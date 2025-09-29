package com.example.myapplication.data.models;

import java.util.HashSet;
import java.util.Set;

public class CreateEventTypeDTO {
    private String name;
    private String description;
    private Set<Integer> recommendedCategoriesIds = new HashSet<>();

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

    public Set<Integer> getRecommendedCategoriesIds() {
        return recommendedCategoriesIds;
    }

    public void setRecommendedCategoriesIds(Set<Integer> recommendedCategoriesIds) {
        this.recommendedCategoriesIds = recommendedCategoriesIds;
    }
}
