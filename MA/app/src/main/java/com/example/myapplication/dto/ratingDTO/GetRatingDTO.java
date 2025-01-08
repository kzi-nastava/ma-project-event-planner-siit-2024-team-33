package com.example.myapplication.dto.ratingDTO;

public class GetRatingDTO {
    private Integer id;
    private String offerName;
    private Integer authorId;
    private String authorName;
    private Integer value;
    private String comment;
    private Boolean isAccepted;
    private Boolean isDeleted;

    public Integer getId() {
        return id;
    }

    public Boolean getAccepted() {
        return isAccepted;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public Integer getValue() {
        return value;
    }

    public String getComment() {
        return comment;
    }

    public String getOfferName() {
        return offerName;
    }

}
