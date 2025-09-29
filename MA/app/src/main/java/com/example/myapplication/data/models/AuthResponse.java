package com.example.myapplication.data.models;

import com.google.gson.annotations.SerializedName;

public class AuthResponse {
    @SerializedName("jwt")
    private String accessToken;

    @SerializedName("expiresIn")
    private int expiresIn;

    public String getAccessToken() {
        return accessToken;
    }

    public int getExpiresIn() {
        return expiresIn;
    }
}
