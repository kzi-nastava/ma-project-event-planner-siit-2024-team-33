package com.example.myapplication.data.dto.invitationDTO;

import com.example.myapplication.data.dto.eventDTO.MinimalEventDTO;

public class SimpleInvitation {
    private int id;
    private MinimalEventDTO event;
    private String status;

    public int getId() {
        return id;
    }
    public MinimalEventDTO getEvent(){
        return event;
    }

    public String getStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEvent(MinimalEventDTO event) {
        this.event = event;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
