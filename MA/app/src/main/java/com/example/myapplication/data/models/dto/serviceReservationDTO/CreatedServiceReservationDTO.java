package com.example.myapplication.data.models.dto.serviceReservationDTO;

import java.time.LocalDate;
import java.time.LocalTime;

public class CreatedServiceReservationDTO {
    private int reservationId;
    private String ServiceName;
    private String EventName;
    private LocalDate ReservationDate;
    private LocalTime StartTime;
    private LocalTime EndTime;
}
