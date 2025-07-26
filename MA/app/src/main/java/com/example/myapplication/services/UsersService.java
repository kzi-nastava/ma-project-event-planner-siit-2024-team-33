package com.example.myapplication.services;

import com.example.myapplication.api.UserApi;
import com.example.myapplication.utils.Settings;

import retrofit2.Call;
import retrofit2.http.Path;

public class UsersService {
    private static final String BASE_URL = Settings.BASE_URL + "/api/events/";
    private final UserApi usersApi;

    public UsersService() {usersApi = ApiClient.getRetrofit(BASE_URL).create(UserApi.class);}

    public Call<String> blockUser(@Path("blockedEmail") String blockedEmail){
        return usersApi.blockUser(blockedEmail);
    }

    public Call<String> unblockUser(@Path("blockedEmail") String blockedEmail){
        return usersApi.unblockUser(blockedEmail);
    }

}
