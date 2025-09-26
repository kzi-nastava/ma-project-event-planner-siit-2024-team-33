package com.example.myapplication.data.services;

import com.example.myapplication.data.api.user.UserApi;
import com.example.myapplication.data.dto.userDTO.GetUserDTO;
import com.example.myapplication.data.dto.userDTO.RegisterUser;
import com.example.myapplication.data.dto.userDTO.RegisteredUser;
import com.example.myapplication.data.dto.userDTO.UpdateUser;
import com.example.myapplication.data.dto.userDTO.UpdatedUser;
import com.example.myapplication.data.models.UpgradeRequest;
import com.example.myapplication.data.models.UpgradeUser;
import com.example.myapplication.utils.Settings;

import retrofit2.Call;
import retrofit2.http.Path;

public class UsersService {
    private static final String BASE_URL = Settings.BASE_URL;
    private final UserApi usersApi;

    public UsersService() {
        usersApi = ApiClient.getRetrofit(BASE_URL).create(UserApi.class);
    }

    public Call<GetUserDTO> getCurrentUser() {
        return usersApi.getCurrentUser();
    }

    public Call<UpdatedUser> updateUser(UpdateUser updateUser) {
        return usersApi.updateUser(updateUser);
    }

    public Call<Void> terminateUser() {
        return usersApi.terminateUser();
    }

    public Call<String> blockUser(@Path("blockedEmail") String blockedEmail){
        return usersApi.blockUser(blockedEmail);
    }
    public Call<RegisteredUser> signup(RegisterUser registerUser) {
        return usersApi.createUser(registerUser);
    }

    public Call<String> unblockUser(@Path("blockedEmail") String blockedEmail){
        return usersApi.unblockUser(blockedEmail);
    }
    public Call<UpgradeRequest> upgradeUser(UpgradeUser upgradeUser) {
        return usersApi.upgradeUser(upgradeUser);
    }
}
