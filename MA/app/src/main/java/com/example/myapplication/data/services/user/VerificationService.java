package com.example.myapplication.data.services.user;

import com.example.myapplication.data.api.user.VerificationAPI;
import com.example.myapplication.data.services.ApiClient;
import com.example.myapplication.utils.Settings;

import retrofit2.Call;

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
