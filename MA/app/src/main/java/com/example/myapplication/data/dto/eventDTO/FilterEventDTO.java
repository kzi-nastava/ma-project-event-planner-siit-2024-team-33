package com.example.myapplication.data.dto.eventDTO;

import java.util.List;

public class FilterEventDTO {
    public String name;
    public String location;
    public String firstPossibleDate;
    public String lastPossibleDate;
    public List<Integer> eventTypes;
    public Integer numOfAttendees;

    public FilterEventDTO(String name, String location,Integer numOfAttendees, String firstPossibleDate, String lastPossibleDate, List<Integer> eventTypes){
        this.eventTypes=eventTypes;
        this.location=location;
        this.name=name;
        this.firstPossibleDate=firstPossibleDate;
        this.lastPossibleDate=lastPossibleDate;
        this.numOfAttendees=numOfAttendees;
    }
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
