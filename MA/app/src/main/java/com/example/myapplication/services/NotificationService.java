package com.example.myapplication.services;

import com.example.myapplication.api.EventApi;
import com.example.myapplication.api.NotificationApi;
import com.example.myapplication.dto.notificationDTO.GetNotificationDTO;
import com.example.myapplication.dto.notificationDTO.PostNotificationDTO;
import com.example.myapplication.dto.notificationDTO.PutNotificationDTO;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NotificationService {
    private static final String BASE_URL = "http://192.168.2.8:8080/api/notifications/";
    private  final NotificationApi notificationApi;

    public NotificationService() {notificationApi = ApiClient.getRetrofit(BASE_URL).create(NotificationApi.class);}

    public Call<Void> sendNotification(PostNotificationDTO postNotificationDTO) {
        return notificationApi.sendNotification(postNotificationDTO);
    }

    public Call<List<GetNotificationDTO>> getNotifications() {
        return notificationApi.getNotifications();
    }

    public Call<Void> deleteNotification(Integer id) {
        return notificationApi.deleteNotification(id);
    }


    public Call<Map<String, String>> updateNotification(Integer id, PutNotificationDTO putNotificationDTO) {
        return notificationApi.updateNotification(id, putNotificationDTO);
    }
}
