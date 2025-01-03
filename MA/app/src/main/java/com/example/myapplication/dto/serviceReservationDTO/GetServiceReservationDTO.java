package com.example.myapplication.dto.serviceReservationDTO;

import com.example.myapplication.models.Availability;

import java.time.LocalDate;
import java.time.LocalTime;

public class GetServiceReservationDTO {
    private int reservationId;
    private String ServiceName;
    private String EventName;
    private String ProviderName;
    private LocalDate ReservationDate;
    private LocalTime StartTime;
    private LocalTime EndTime;
    private Availability Status;
}
