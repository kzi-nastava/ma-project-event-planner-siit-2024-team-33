package com.example.myapplication.data.services;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.data.dto.chatDTO.ChatContactDTO;
import com.example.myapplication.data.dto.chatDTO.MessageDTO;
import com.example.myapplication.data.dto.chatDTO.PostMessageDTO;
import com.example.myapplication.utils.Settings;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompHeader;

public class ChatWebsocketService {
    private static ChatWebsocketService instance;


    private StompClient stompClient;
    private boolean isConnected = false;

    private MutableLiveData<MessageDTO> messageLiveData = new MutableLiveData<>();
    private MutableLiveData<ChatContactDTO> chatTargetLiveData = new MutableLiveData<>();

    private List<Runnable> onConnectCallbacks = new ArrayList<>();
    private List<Runnable> onDisconnectCallbacks = new ArrayList<>();

    private final String wsUrl = Settings.WEBSOCKET_URL;
    private final String token = getJwtToken();

    private ChatWebsocketService() {}

    public static ChatWebsocketService getInstance() {
        if (instance == null) {
            instance = new ChatWebsocketService();
        }
        return instance;
    }

    @SuppressLint("CheckResult")
    public void connect() {
        if(getJwtToken() == null)
            return;

        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, wsUrl);

        List<StompHeader> headers = new ArrayList<>();
        headers.add(new StompHeader("Authorization", "Bearer " + getJwtToken()));

        stompClient.connect(headers);

        stompClient.lifecycle().subscribe(lifecycleEvent -> {
            switch (lifecycleEvent.getType()) {
                case OPENED:
                    isConnected = true;
                    for (Runnable callback : onConnectCallbacks) callback.run();

                    stompClient.topic("/user/queue/messages").subscribe(topicMessage -> {
                        String json = topicMessage.getPayload();
                        MessageDTO msg = new Gson().fromJson(json, MessageDTO.class);
                        messageLiveData.postValue(msg);
                    });
                    break;

                case ERROR:
                    Log.e("ChatService", "Error", lifecycleEvent.getException());
                    break;

                case CLOSED:
                    Log.i("GAS", "STOMP DISCONNECTED");
                    isConnected = false;
                    for (Runnable callback : onDisconnectCallbacks) callback.run();
                    break;
            }
        });
    }

    public void disconnect() {
        if (stompClient != null && stompClient.isConnected()) {
            stompClient.disconnect();
        }
    }

    @SuppressLint("CheckResult")
    public void sendMessage(String message, String recipientEmail) {
        PostMessageDTO msg = new PostMessageDTO();
        msg.message = message;
        msg.recipientEmail = recipientEmail;
        String json = new Gson().toJson(msg);

        if (stompClient != null && isConnected) {
            stompClient.send("/api/chat.send", json)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> Log.d("Gas", "Message sent successfully"),
                            throwable -> {
                                Log.e("gas", "Error sending message", throwable);
                                throwable.printStackTrace();
                            });
        }
    }

    public LiveData<MessageDTO> getMessages() {
        return messageLiveData;
    }

    public void openChatWith(ChatContactDTO contact) {
        chatTargetLiveData.postValue(contact);
    }

    public LiveData<ChatContactDTO> getOpenChatTarget() {
        return chatTargetLiveData;
    }

    public void onConnect(Runnable callback) {
        onConnectCallbacks.add(callback);
    }

    public void onDisconnect(Runnable callback) {
        onDisconnectCallbacks.add(callback);
    }

    private String getJwtToken() {
        return AuthenticationService.getJwtToken();
    }
}