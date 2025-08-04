package com.example.myapplication.dto.LoginDTO;

import com.google.gson.annotations.SerializedName;

public class AuthResponse {
    @SerializedName("accessToken")
    private String accessToken;
    private int expiresIn;
    public String getAccessToken() {
        return accessToken;
    }

    public int getExpiresIn(){
        return expiresIn;
    }

}
