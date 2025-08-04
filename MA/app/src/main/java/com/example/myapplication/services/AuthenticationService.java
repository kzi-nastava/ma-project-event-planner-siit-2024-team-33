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
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthenticationService {
    private static AuthService authService;
    private static ChatWebsocketService chatWebsocketService;
    private static SharedPreferences prefs;
    private static Context context;
    private static final String PREFS_NAME = "auth";
    private static final String KEY_JWT = "jwt";
    private static final String KEY_ROLE = "role";

    public AuthenticationService(Context context) {
        this.context = context;
        this.prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        this.authService = ApiClient.getClient(context).create(AuthService.class);
    }

    public void login(String email, String password, Callback<AuthResponse> callback) {
        LoginRequest request = new LoginRequest(email, password);

        authService.login(request).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> loginCall, Response<AuthResponse> loginResponse) {
                if (loginResponse.isSuccessful() && loginResponse.body() != null) {
                    String token = loginResponse.body().getAccessToken();
                    prefs.edit().putString(KEY_JWT, token).apply();

                    chatWebsocketService = ChatWebsocketService.getInstance();
                    chatWebsocketService.connect();

                    UserApi userApi = ApiClient.getClient(context).create(UserApi.class);
                    userApi.getCurrentUser().enqueue(new Callback<GetUserDTO>() {
                        @Override
                        public void onResponse(Call<GetUserDTO> userCall, Response<GetUserDTO> userResponse) {
                            if (userResponse.isSuccessful() && userResponse.body() != null) {
                                GetUserDTO dto = userResponse.body();
                                AuthentifiedUser user = new AuthentifiedUser(dto);

                                String userJson = new Gson().toJson(user);
                                prefs.edit().putString("user", userJson).apply();
                            }
                            callback.onResponse(loginCall, loginResponse);
                        }

                        @Override
                        public void onFailure(Call<GetUserDTO> call, Throwable t) {
                            Log.e("AuthService", "Failed to fetch user", t);
                            callback.onResponse(loginCall, loginResponse);
                        }
                    });

                } else {
                    callback.onFailure(loginCall, new Throwable("Login failed: " + loginResponse.code()));
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

    public static AuthentifiedUser getLoggedInUser() {
        String json = prefs.getString("user", null);
        if (json != null) {
            return new Gson().fromJson(json, AuthentifiedUser.class);
        }
        return null;
    }

    public static String getJwtToken() {
        if (prefs == null && context != null) {
            prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        }
        return prefs != null ? prefs.getString(KEY_JWT, null) : null;
    }

    public static void logout() {
        prefs.edit()
                .remove(KEY_JWT)
                .remove(KEY_ROLE)
                .remove("user")
                .apply();
        chatWebsocketService = ChatWebsocketService.getInstance();
        chatWebsocketService.disconnect();
    }
}
