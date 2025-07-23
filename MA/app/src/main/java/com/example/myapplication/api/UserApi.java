package com.example.myapplication.api;
import com.example.myapplication.dto.userDTO.GetUserDTO;
import com.example.myapplication.dto.userDTO.RegisterUser;
import com.example.myapplication.dto.userDTO.RegisteredUser;
import com.example.myapplication.dto.userDTO.UpdateUser;
import com.example.myapplication.dto.userDTO.UpdatedUser;
import com.example.myapplication.models.AuthentifiedUser;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserApi {

    @GET("/api/users/me")
    Call<GetUserDTO> getCurrentUser();
    @POST("/api/users/signup")
    Call<RegisteredUser> createUser(@Body RegisterUser registerUser);

    @POST("/api/users/update/profile")
    Call<UpdatedUser> updateUser(@Body UpdateUser updateUser);

//    @POST("/api/users/update/password")
//    Call<Void> updatePassword(@Body UpdatePassword updatePassword);

    @GET("/api/users/terminate/profile")
    Call<Boolean> terminateUser();

    @POST("/api/users/block/{blockedEmail}")
    Call<String> blockUser(@Path("blockedEmail") String blockedEmail);

    @DELETE("/api/users/block/{blockedEmail}")
    Call<String> unblockUser(@Path("blockedId") String blockedEmail);
}


