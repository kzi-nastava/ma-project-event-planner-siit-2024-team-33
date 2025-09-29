package com.example.myapplication.data.models.dto;

import java.util.Set;

public class CreatedEventTypeDTO {
    private Integer id;
    private String name;
    private String description;
    private Boolean isActive;
    private Set<Integer> recommendedCategories;

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

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Set<Integer> getRecommendedCategories() {
        return recommendedCategories;
    }

    public void setRecommendedCategories(Set<Integer> recommendedCategories) {
        this.recommendedCategories = recommendedCategories;
    }
}
