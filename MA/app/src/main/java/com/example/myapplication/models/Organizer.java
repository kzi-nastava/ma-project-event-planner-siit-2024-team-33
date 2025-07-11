package com.example.myapplication.models;

public class Organizer extends AuthentifiedUser{
    public String residency;
    public String phoneNumber;

     public String getResidency(){
         return this.residency;
     }
     public String getPhoneNumber(){
         return this.phoneNumber;
     }

     public void setResidency(String residency){
         this.residency = residency;
     }
     public void setPhoneNumber(String phoneNumber){
         this.phoneNumber = phoneNumber;
     }
}
