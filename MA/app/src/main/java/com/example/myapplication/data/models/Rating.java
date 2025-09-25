package com.example.myapplication.data.models;

public class Rating {
    private Integer id;
    private Integer ratingValue;
    private String comment;
    private Boolean accepted;
    private Boolean isDeleted;
    private AuthentifiedUser author;
    private Offer offer;

    public Rating(int id, AuthentifiedUser author,
                  int ratingValue, String comment, boolean accepted, boolean isDeleted) {
        this.id = id;
        this.author = author;
        this.ratingValue = ratingValue;
        this.comment = comment;
        this.accepted = accepted;
        this.isDeleted = isDeleted;
    }


    public int getId() { return id; }
    public int getRatingValue() { return ratingValue; }
    public String getComment() { return comment; }
    public boolean isAccepted() { return accepted; }
    public boolean isDeleted() { return isDeleted; }
}
