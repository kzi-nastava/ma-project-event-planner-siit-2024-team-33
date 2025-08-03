package com.example.myapplication.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class OfferReservation {
    public Integer id;
    public LocalDate dateOfReservation;
    public Offer offer;
    public Event event;
    public LocalDateTime startTime;
    public LocalDateTime endTime;
}
