package com.example.myapplication.data.api;

import com.example.myapplication.data.dto.LoginDTO.AuthResponse;
import com.example.myapplication.data.dto.userDTO.GetUserDTO;
import com.example.myapplication.data.dto.userDTO.RegisterUser;
import com.example.myapplication.data.dto.userDTO.RegisteredUser;
import com.example.myapplication.data.dto.userDTO.UpdatePassword;
import com.example.myapplication.data.dto.userDTO.UpdateUser;
import com.example.myapplication.data.dto.userDTO.UpdatedUser;
import com.example.myapplication.data.models.UpgradeRequest;
import com.example.myapplication.data.models.UpgradeUser;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserApi {

    @GET("/api/users/me")
    Call<GetUserDTO> getCurrentUser();
    @POST("/api/users/signup")
    Call<RegisteredUser> createUser(@Body RegisterUser registerUser);

    @PUT("/api/users/update/profile")
    Call<UpdatedUser> updateUser(@Body UpdateUser updateUser);

    @PUT("/api/users/update/password")
    Call<AuthResponse> updatePassword(@Body UpdatePassword updatePassword);

    @DELETE("/api/users/terminate/profile")
    Call<Void> terminateUser();

    @POST("/api/users/block/{blockedEmail}")
    Call<String> blockUser(@Path("blockedEmail") String blockedEmail);

    @DELETE("/api/users/block/{blockedEmail}")
    Call<String> unblockUser(@Path("blockedId") String blockedEmail);

    @POST("/api/users/me/upgrade")
    Call<UpgradeRequest> upgradeUser(@Body UpgradeUser upgradeUser);
}


