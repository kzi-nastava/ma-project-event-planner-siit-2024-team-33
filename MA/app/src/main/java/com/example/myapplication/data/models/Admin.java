package com.example.myapplication.data.models;

import com.example.myapplication.data.dto.userDTO.GetUserDTO;

public class Admin extends AuthentifiedUser {
    public Admin(GetUserDTO dto){
        super(dto);
    }
}
