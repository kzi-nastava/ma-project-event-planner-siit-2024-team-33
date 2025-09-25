package com.example.myapplication.data.dto.serviceReservationDTO;

import com.example.myapplication.data.models.Availability;

public class GetServiceReservationDTO {
    private int reservationId;
    private String serviceName;
    private String eventName;
    private String providerName;
    private String reservationDate;
    private String startTime;
    private String endTime;
    private Availability status;

    public int getReservationId() {
        return reservationId;
    }
    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public String getServiceName() {
        return serviceName;
    }
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getEventName() {
        return eventName;
    }
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getProviderName() {
        return providerName;
    }
    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getReservationDate() {
        return reservationDate;
    }
    public void setReservationDate(String reservationDate) {
        this.reservationDate = reservationDate;
    }

    public String getStartTime() {
        return startTime;
    }
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Availability getStatus() {
        return status;
    }
    public void setStatus(Availability status) {
        this.status = status;
    }
}
