package com.example.myapplication.data.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface VerificationAPI {
    @GET("/verify")
    Call<String> verifyToken(@Query("token") String token);
}
