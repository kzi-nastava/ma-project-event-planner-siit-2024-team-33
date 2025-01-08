package com.example.myapplication.dto.reportDTO;

public class GetReportDTO {
    private Integer id;
    private String content;
    private String author;
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
