package com.example.myapplication.data.models.dto.reportDTO;

import com.google.gson.annotations.SerializedName;

public class GetReportDTO {
    private Integer id;
    private String content;
    @SerializedName("reporter")
    private String author;
    @SerializedName("reported")
    private String receiver;
    private String dateOfSending;
    private Integer receiverId;

    public Integer getId(){
        return id;
    }
    public String getContent(){
        return content;
    }
    public String getAuthor(){
        return author;
    }
    public String getReceiver() {
        return receiver;
    }

    public String getDateOfSending() {
        return dateOfSending;
    }
    public Integer getReceiverId() {
        return receiverId;
    }
}
