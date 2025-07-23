package com.example.myapplication.services;

import com.example.myapplication.api.UserApi;

import retrofit2.Call;
import retrofit2.http.Path;

public class UsersService {
    private static final String BASE_URL = "http://192.168.2.8:8080/api/events/";
    private final UserApi usersApi;

    public UsersService() {usersApi = ApiClient.getRetrofit(BASE_URL).create(UserApi.class);}

    public Call<String> blockUser(@Path("blockedEmail") String blockedEmail){
        return usersApi.blockUser(blockedEmail);
    }

    public Call<String> unblockUser(@Path("blockedEmail") String blockedEmail){
        return usersApi.unblockUser(blockedEmail);
    }

}
