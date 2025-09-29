package com.example.myapplication.data.models.dto.ratingDTO;

public class PostRatingDTO {
    private int value;
    private String comment;

    public int getValue() {
        return value;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
