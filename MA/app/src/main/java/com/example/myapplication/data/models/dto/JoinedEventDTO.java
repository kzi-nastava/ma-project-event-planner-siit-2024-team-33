package com.example.myapplication.data.models.dto;

public class JoinedEventDTO {
    private Integer id;
    private Integer numOfAttendees;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumOfAttendees() {
        return numOfAttendees;
    }

    public void setNumOfAttendees(Integer numOfAttendees) {
        this.numOfAttendees = numOfAttendees;
    }

    public Integer getNumOfCurrentlyApplied() {
        return numOfCurrentlyApplied;
    }

    public void setNumOfCurrentlyApplied(Integer numOfCurrentlyApplied) {
        this.numOfCurrentlyApplied = numOfCurrentlyApplied;
    }

    private Integer numOfCurrentlyApplied;

    public JoinedEventDTO() {
    }
}
