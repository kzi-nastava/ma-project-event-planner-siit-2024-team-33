package com.example.myapplication.models;

import java.time.LocalDateTime;
import java.util.List;

public class Offer {
    private Integer id;
    private Integer offerID;
    private OfferType type;
    private String name;
    private String description;
    private Double price;
    private Double discount;
    private List<String> pictures;
    private Availability availability;
    private LocalDateTime creationDate;
    private Boolean isPending;
    private Boolean isDeleted;
    private String city;
}
