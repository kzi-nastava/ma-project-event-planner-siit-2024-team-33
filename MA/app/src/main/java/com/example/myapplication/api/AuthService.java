package com.example.myapplication.api;

import com.example.myapplication.dto.LoginDTO.AuthResponse;
import com.example.myapplication.dto.LoginDTO.LoginRequest;
import com.example.myapplication.models.AuthentifiedUser;
import com.example.myapplication.models.EmailRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AuthService {
    @POST("/api/auth/login")
    Call<AuthResponse> login(@Body LoginRequest loginRequest);

    @POST("/api/auth/check-email")
    Call<Boolean> checkEmailAvailability(@Body EmailRequest emailRequest);

}
