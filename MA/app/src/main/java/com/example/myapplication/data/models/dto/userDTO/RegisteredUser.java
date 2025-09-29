package com.example.myapplication.data.models.dto.userDTO;

import com.example.myapplication.data.models.AuthentifiedUser;
import com.example.myapplication.data.models.Organizer;
import com.example.myapplication.data.models.Provider;

public class RegisteredUser extends RegisterUser{
    private int id;

    public void setId(int id) {
        this.id = id;
    }
    public int getId(){
        return this.id;
    }

    public RegisteredUser(Organizer organizer) {
        this.setId(organizer.getId());
        super.setEmail(organizer.getEmail());
        super.setPassword(organizer.getPassword());
        super.setName(organizer.getName());
        super.setSurname(organizer.getSurname());
        super.setPicture(organizer.getPicture());
        super.setResidency(organizer.getResidency());
        super.setPhoneNumber(organizer.getPhoneNumber());
        super.setRole(organizer.getRole().getName());
    }

    public RegisteredUser(Provider provider) {
        this.setId(provider.getId());
        super.setEmail(provider.getEmail());
        super.setPassword(provider.getPassword());
        super.setName(provider.getName());
        super.setSurname(provider.getSurname());
        super.setPicture(provider.getPicture());
        super.setPictures(provider.getPictures());
        super.setDescription(provider.getDescription());
        super.setPhoneNumber(provider.getPhoneNumber());
        super.setProviderName(provider.getProviderName());
        super.setResidency(provider.getResidency());
        super.setRole(provider.getRole().getName());
    }

    public RegisteredUser(AuthentifiedUser auser) {
        this.setId(auser.getId());
        super.setEmail(auser.getEmail());
        super.setPassword(auser.getPassword());
        super.setName(auser.getName());
        super.setSurname(auser.getSurname());
        super.setPicture(auser.getPicture());
        super.setRole(auser.getRole().getName());
    }
}
