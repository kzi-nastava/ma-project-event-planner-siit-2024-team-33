package com.example.myapplication.data.services.communication;

import com.example.myapplication.data.api.communication.InvitationApi;
import com.example.myapplication.data.models.dto.invitationDTO.GetInvitationDTO;
import com.example.myapplication.data.models.dto.invitationDTO.PostInvitationDTO;
import com.example.myapplication.data.models.dto.invitationDTO.SimpleInvitation;
import com.example.myapplication.data.services.ApiClient;
import com.example.myapplication.utils.Settings;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Callback;

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