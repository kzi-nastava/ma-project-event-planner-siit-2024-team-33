package com.example.myapplication.data.models;

import com.example.myapplication.data.dto.userDTO.GetUserDTO;

public class Organizer extends AuthentifiedUser{
    public String residency;
    public String phoneNumber;

     public String getResidency(){
         return this.residency;
     }
     public String getPhoneNumber(){
         return this.phoneNumber;
     }
    public Organizer(GetUserDTO dto) {
        super(dto);
        this.residency = dto.getResidency();
        this.phoneNumber = dto.getPhoneNumber();
    }
     public void setResidency(String residency){
         this.residency = residency;
     }
     public void setPhoneNumber(String phoneNumber){
         this.phoneNumber = phoneNumber;
     }
}
