package com.example.myapplication.services;

import com.example.myapplication.api.InvitationApi;
import com.example.myapplication.dto.invitationDTO.GetInvitationDTO;
import com.example.myapplication.dto.invitationDTO.PostInvitationDTO;
import com.example.myapplication.dto.invitationDTO.SimpleInvitation;
import com.example.myapplication.utils.Settings;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InvitationService {
    private static final String BASE_URL = Settings.BASE_URL + "/api/events/";
    private final InvitationApi invitationApi;

    public InvitationService() {invitationApi = ApiClient.getRetrofit(BASE_URL).create(InvitationApi.class);}

    public void createInvitations(PostInvitationDTO dto, Callback<Void> callback) {
        invitationApi.createInvitations(dto).enqueue(callback);
    }

    public void updateInvitationStatus(int id, String status, Callback<Void> callback) {
        Map<String, String> map = new HashMap<>();
        map.put("status", status);
        invitationApi.updateInvitationStatus(id, map).enqueue(callback);
    }

    public void getMyPendingInvitations(Callback<List<SimpleInvitation>> callback) {
        invitationApi.getMyPendingInvitations().enqueue(callback);
    }

    public void getInvitationsForEvent(int eventId, Callback<List<GetInvitationDTO>> callback) {
        invitationApi.getInvitationsForEvent(eventId).enqueue(callback);
    }

    public void getInvitationById(int id, Callback<GetInvitationDTO> callback) {
        invitationApi.getInvitationById(id).enqueue(callback);
    }

    public void deleteInvitation(int id, Callback<Void> callback) {
        invitationApi.deleteInvitation(id).enqueue(callback);
    }
}