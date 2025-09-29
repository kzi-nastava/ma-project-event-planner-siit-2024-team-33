package com.example.myapplication.data.api.user;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import com.example.myapplication.data.models.VerificationResponse;

public interface VerificationAPI {

    @GET("/api/users/verification")
    Call<VerificationResponse> verifyToken(@Query("token") String token);
}
