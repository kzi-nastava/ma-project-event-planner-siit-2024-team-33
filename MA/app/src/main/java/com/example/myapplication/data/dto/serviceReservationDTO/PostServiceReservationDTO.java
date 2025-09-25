package com.example.myapplication.data.dto.serviceReservationDTO;

public class PostServiceReservationDTO {
    private Integer eventId;

    private String reservationDate;
    private String startTime;
    private String endTime;


    public PostServiceReservationDTO(Integer eventId, String reservationDate, String startTime, String endTime){
        this.eventId=eventId;
        this.reservationDate=reservationDate;
        this.startTime=startTime;
        this.endTime=endTime;
    }
}
