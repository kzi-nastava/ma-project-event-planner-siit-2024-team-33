package com.example.myapplication.services;

import android.util.Log;

import com.example.myapplication.api.UserApi;
import com.example.myapplication.api.VerificationAPI;
import com.example.myapplication.utils.Settings;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VerificationService {

    private final VerificationAPI verificationApi;
    private static final String BASE_URL = Settings.BASE_URL + "/api";

    public VerificationService() {verificationApi = ApiClient.getRetrofit(BASE_URL).create(VerificationAPI.class);}

    public Call<String> verifyToken(String token) {
        return verificationApi.verifyToken(token);
    }

    public interface VerificationCallback {
        void onSuccess(String result);
        void onFailure(String errorMessage);
    }
}
