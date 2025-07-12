package com.example.myapplication.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.myapplication.api.AuthService;
import com.example.myapplication.api.UserApi;
import com.example.myapplication.dto.LoginDTO.AuthResponse;
import com.example.myapplication.dto.LoginDTO.LoginRequest;
import com.example.myapplication.dto.userDTO.GetUserDTO;
import com.example.myapplication.models.AuthentifiedUser;
import com.example.myapplication.services.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthenticationService {
    private static AuthService authService;
    private static SharedPreferences prefs;

    private static final String PREFS_NAME = "auth";
    private static final String KEY_JWT = "jwt";
    private static final String KEY_ROLE = "role";

    public AuthenticationService(Context context) {
        this.prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        this.authService = ApiClient.getClient(context).create(AuthService.class);
    }

    public static void login(String email, String password, Callback<AuthResponse> callback) {
        LoginRequest request = new LoginRequest(email, password);
        authService.login(request).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getAccessToken();
                    prefs.edit().putString(KEY_JWT, token).apply();
                    Log.d("GAS",token);
                   // ApiClient.reset();

                    callback.onResponse(call, response);
                } else {
                    callback.onFailure(call, new Throwable("Login failed: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                callback.onFailure(call, t);
            }
        });
    }

    public static boolean isLoggedIn() {
        String token = getJwtToken();
        return token != null && !token.isEmpty();
    }

    public static String getJwtToken() {
        return prefs.getString(KEY_JWT, null);
    }

    public void logout() {
        prefs.edit().remove(KEY_JWT).remove(KEY_ROLE).apply();
    }
}
