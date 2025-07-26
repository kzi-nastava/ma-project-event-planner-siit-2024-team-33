package com.example.myapplication.services;

import com.example.myapplication.api.InvitationApi;
import com.example.myapplication.dto.invitationDTO.GetInvitationDTO;
import com.example.myapplication.dto.invitationDTO.PostInvitationDTO;
import com.example.myapplication.utils.Settings;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InvitationService {
    private static final String BASE_URL = Settings.BASE_URL + "/api/invitations/";
    private final InvitationApi invitationApi;

    public InvitationService() {invitationApi = ApiClient.getRetrofit(BASE_URL).create(InvitationApi.class);}

    public Call<Void> createInvitations(Integer inviterId, PostInvitationDTO postInvitationDTO) {
        return invitationApi.createInvitations(inviterId, postInvitationDTO);
    }

    public Call<List<GetInvitationDTO>> getInvitationsForEvent(Integer eventId) {
        return invitationApi.getInvitationsForEvent(eventId);
    }
}
