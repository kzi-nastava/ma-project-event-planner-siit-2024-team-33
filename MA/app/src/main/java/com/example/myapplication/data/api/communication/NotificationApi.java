package com.example.myapplication.data.api.communication;

import com.example.myapplication.data.models.dto.notificationDTO.GetNotificationDTO;
import com.example.myapplication.data.models.dto.notificationDTO.PostNotificationDTO;
import com.example.myapplication.data.models.dto.notificationDTO.PutNotificationDTO;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface NotificationApi {

    @POST("notifications")
    Call<Void> sendNotification(
            @Body PostNotificationDTO postNotificationDTO
    );

    @GET("notifications")
    Call<List<GetNotificationDTO>> getNotifications();

    @DELETE("notifications/{id}")
    Call<Void> deleteNotification(
            @Path("id") Integer id
    );

    @PUT("notifications/{id}")
    Call<Map<String, String>> updateNotification(
            @Path("id") Integer id,
            @Body PutNotificationDTO putNotificationDTO
    );
}
