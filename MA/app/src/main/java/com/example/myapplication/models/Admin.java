package com.example.myapplication.models;

import com.example.myapplication.dto.userDTO.GetUserDTO;

public class Admin extends AuthentifiedUser {
    public Admin(GetUserDTO dto){
        super(dto);
    }
}
