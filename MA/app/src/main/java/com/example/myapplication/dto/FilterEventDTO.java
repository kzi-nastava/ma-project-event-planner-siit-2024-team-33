package com.example.myapplication.dto;

import java.util.List;

public class FilterEventDTO {
    public String name;
    public String location;
    public String firstPossibleDate;
    public String lastPossibleDate;
    public List<MinimalEventTypeDTO> eventTypes;
    public Integer numOfAttendees;

    public String getName(){
        return name;
    }
    public String getLocation(){
        return location;
    }
    public String getFirstPossibleDate(){
        return firstPossibleDate;
    }
    public String getLastPossibleDate(){
        return lastPossibleDate;
    }
    public List<MinimalEventTypeDTO> getEventTypes(){
        return eventTypes;
    }
    public Integer getNumOfAttendees(){
        return numOfAttendees;
    }
}
