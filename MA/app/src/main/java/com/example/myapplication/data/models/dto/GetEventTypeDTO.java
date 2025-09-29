package com.example.myapplication.data.models.dto;

import java.io.Serializable;
import java.util.Set;

public class GetEventTypeDTO implements Serializable {
    private Integer id;
    private String description;
    private String name;
    private Boolean isActive;
    private Set<Integer> recommendedCategories;

    public GetEventTypeDTO() {}

    public GetEventTypeDTO(UpdatedEventTypeDTO updated) {
        this.id = updated.getId();
        this.description = updated.getDescription();
        this.name = updated.getName();
        this.isActive = updated.getActive();
        this.recommendedCategories = updated.getRecommendedCategories();
    }
    // Getters and setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public Set<Integer> getRecommendedCategories() { return recommendedCategories; }
    public void setRecommendedCategories(Set<Integer> recommendedCategories) {
        this.recommendedCategories = recommendedCategories;
    }
}