package com.example.myapplication.api;

import com.example.myapplication.dto.notificationDTO.GetNotificationDTO;
import com.example.myapplication.dto.notificationDTO.PostNotificationDTO;
import com.example.myapplication.dto.notificationDTO.PutNotificationDTO;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NotificationApi {

    @POST
    Call<Void> sendNotification(
            @Body PostNotificationDTO postNotificationDTO
    );

    @GET
    Call<List<GetNotificationDTO>> getNotifications(
            @Query("receiverId") int receiverId
    );

    @DELETE("{id}")
    Call<Void> deleteNotification(
            @Path("id") Integer id
    );

    @PUT("{id}")
    Call<Map<String, String>> updateNotification(
            @Path("id") Integer id,
            @Body PutNotificationDTO putNotificationDTO
    );
}
