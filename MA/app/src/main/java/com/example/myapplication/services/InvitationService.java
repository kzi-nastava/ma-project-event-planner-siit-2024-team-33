package com.example.myapplication.services;

import com.example.myapplication.api.InvitationApi;
import com.example.myapplication.dto.invitationDTO.GetInvitationDTO;
import com.example.myapplication.dto.invitationDTO.PostInvitationDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InvitationService {
    private static final String BASE_URL = "http://192.168.2.8:8080/api/invitations/";
    private final InvitationApi invitationApi;

    public InvitationService() {invitationApi = ApiClient.getRetrofit(BASE_URL).create(InvitationApi.class);}

    public Call<Void> createInvitations(Integer inviterId, PostInvitationDTO postInvitationDTO) {
        return invitationApi.createInvitations(inviterId, postInvitationDTO);
    }

    public Call<List<GetInvitationDTO>> getInvitationsForEvent(Integer eventId) {
        return invitationApi.getInvitationsForEvent(eventId);
    }
}
