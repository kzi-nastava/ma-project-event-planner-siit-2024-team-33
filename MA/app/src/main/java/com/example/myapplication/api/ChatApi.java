package com.example.myapplication.api;

import com.example.myapplication.dto.chatDTO.ChatContactDTO;
import com.example.myapplication.dto.chatDTO.MessageDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ChatApi {
    @GET("api/chat/all")
    public Call<List<ChatContactDTO>> getContacts();

    @GET("api/chat/{email}")
    public Call<List<MessageDTO>> getMessagesWith(@Path("email") String email);
}
