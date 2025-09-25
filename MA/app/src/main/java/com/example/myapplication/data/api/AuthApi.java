package com.example.myapplication.data.api;

import com.example.myapplication.data.dto.LoginDTO.AuthResponse;
import com.example.myapplication.data.dto.LoginDTO.LoginRequest;
import com.example.myapplication.data.models.EmailRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AuthApi {
    @POST("/api/auth/login")
    Call<AuthResponse> login(@Body LoginRequest loginRequest);

    @GET("/api/auth/check-email")
    Call<Boolean> checkEmailAvailability(@Query("email") String email);
}
