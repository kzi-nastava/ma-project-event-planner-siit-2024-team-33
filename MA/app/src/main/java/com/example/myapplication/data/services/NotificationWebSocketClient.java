package com.example.myapplication.data.services;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.example.myapplication.data.dto.notificationDTO.GetNotificationDTO;
import com.example.myapplication.data.services.authentication.AuthenticationService;
import com.example.myapplication.utils.NotificationUtils;
import com.example.myapplication.utils.Settings;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompHeader;

public class NotificationWebSocketClient {
    private StompClient stompClient;
    private final Context context;
    private final String baseUrl = Settings.WEBSOCKET_URL;

    public NotificationWebSocketClient(Context context) {
        this.context = context;
    }

    @SuppressLint("CheckResult")
    public void connect() {
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, baseUrl);

        String jwtToken = AuthenticationService.getJwtToken();
        if(jwtToken == null || jwtToken.isEmpty())
            return;

        List<StompHeader> headers = new ArrayList<>();
        headers.add(new StompHeader("Authorization", "Bearer " + jwtToken));

        stompClient.connect(headers);

        stompClient.lifecycle().subscribe(lifecycleEvent -> {
            switch (lifecycleEvent.getType()) {
                case OPENED:
                    Log.d("WebSocketGAS", "Connected");
                    subscribeToNotifications();
                    break;
                case ERROR:
                    Log.e("WebSocketGAS", "Error", lifecycleEvent.getException());
                    break;
                case CLOSED:
                    Log.d("WebSocketGAS", "Closed");
                    break;
            }
        });
    }


    private void subscribeToNotifications() {
        stompClient.topic("/user/queue/notifications").subscribe(topicMessage -> {
            Log.d("WebSocket", "Received: " + topicMessage.getPayload());

            GetNotificationDTO notification = new Gson().fromJson(topicMessage.getPayload(), GetNotificationDTO.class);

            Date date = null;
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
                sdf.setTimeZone(TimeZone.getTimeZone("UTC")); // Adjust if needed
                date = sdf.parse(notification.getDateOfSending());
            } catch (Exception e) {
                e.printStackTrace();
            }

            NotificationUtils.showNotification(
                    context,
                    notification.getContent(),
                    date
            );
        });
    }

    public void disconnect() {
        if (stompClient != null) stompClient.disconnect();
    }
}
