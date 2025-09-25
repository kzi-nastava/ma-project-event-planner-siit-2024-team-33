package com.example.myapplication.data.dto.reportDTO;

public class PostReportDTO {
    private Integer reportedUserId;
    private String content;

    public PostReportDTO(Integer reportedUserId, String content) {
        this.reportedUserId = reportedUserId;
        this.content = content;
    }

    public Integer getReportedUserId() {
        return reportedUserId;
    }

    public void setReportedUserId(Integer reportedUserId) {
        this.reportedUserId = reportedUserId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
