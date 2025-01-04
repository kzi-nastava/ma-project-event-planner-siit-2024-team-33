package com.example.myapplication.dto.eventDTO;

import java.util.List;

public class FilterEventDTO {
    public String name;
    public String location;
    public String firstPossibleDate;
    public String lastPossibleDate;
    public List<Integer> eventTypes;
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
    public List<Integer> getEventTypes(){
        return eventTypes;
    }
    public Integer getNumOfAttendees(){
        return numOfAttendees;
    }
}
