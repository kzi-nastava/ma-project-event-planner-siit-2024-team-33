package com.example.myapplication.data.services.communication;

import com.example.myapplication.data.api.communication.NotificationApi;
import com.example.myapplication.data.models.dto.notificationDTO.GetNotificationDTO;
import com.example.myapplication.data.models.dto.notificationDTO.PostNotificationDTO;
import com.example.myapplication.data.models.dto.notificationDTO.PutNotificationDTO;
import com.example.myapplication.data.services.ApiClient;
import com.example.myapplication.utils.Settings;

import java.util.List;
import java.util.Map;

import retrofit2.Call;

public class NotificationService {
    private static final String BASE_URL = Settings.BASE_URL + "/api/";
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
