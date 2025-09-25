package com.example.myapplication.data.services;

import com.example.myapplication.data.api.ChatApi;
import com.example.myapplication.data.dto.chatDTO.ChatContactDTO;
import com.example.myapplication.data.dto.chatDTO.MessageDTO;
import com.example.myapplication.utils.Settings;

import java.util.List;

import retrofit2.Call;

public class ChatService {
    private static final String BASE_URL = Settings.BASE_URL + "/";
    private final ChatApi chatApi;

    public ChatService() {
        chatApi = ApiClient.getRetrofit(BASE_URL).create(ChatApi.class);
    }

    public Call<List<ChatContactDTO>> getContacts() {
        return chatApi.getContacts();
    }

    public Call<List<MessageDTO>> getMessagesWith(String email){
        return  chatApi.getMessagesWith(email);
    }
}
