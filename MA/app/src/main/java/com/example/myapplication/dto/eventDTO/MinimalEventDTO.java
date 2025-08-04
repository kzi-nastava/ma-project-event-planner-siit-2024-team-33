package com.example.myapplication.dto.eventDTO;

import com.example.myapplication.dto.eventTypeDTO.MinimalEventTypeDTO;

public class MinimalEventDTO{
    private Integer id;
    private String name;
    private String description;
    private MinimalEventTypeDTO validEvent;

    @Override
    public String toString(){
        return name;
    }

    public Integer getId() {
        return id;
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

    public MinimalEventTypeDTO getValidEvent() {
        return validEvent;
    }

    public void setValidEvent(MinimalEventTypeDTO validEvent) {
        this.validEvent = validEvent;
    }
}
